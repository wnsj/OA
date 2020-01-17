package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.bean.RestDownBean;
import com.jiubo.oa.bean.RestReasonBean;
import com.jiubo.oa.dao.RestDownDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmployeeService;
import com.jiubo.oa.service.RestDownService;
import com.jiubo.oa.service.RestReasonService;
import com.jiubo.oa.service.WxSendMessageService;
import com.jiubo.oa.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:倒休服务实现类
 * @date: 2020-01-07 15:34
 * @author: dx
 * @version: 1.0
 */
@Service
public class RestDownServiceImpl extends ServiceImpl<RestDownDao, RestDownBean> implements RestDownService {

    //相差天数
    @Value("${differDay}")
    private int differDay;

    @Autowired
    private RestDownDao restDownDao;

    //倒休修改页面
    @Value("${modifyRestDownUrl}")
    private String modifyRestDownUrl;

    //倒休审核页面
    @Value("${examineRestDownUrl}")
    private String examineRestDownUrl;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RestReasonService restReasonService;

    @Autowired
    private WxSendMessageService wxSendMessageService;

    @Override
    public List<RestDownBean> queryRestDown(RestDownBean restDownBean) throws Exception {
        if (restDownBean != null && StringUtils.isNotBlank(restDownBean.getCreateEndDate())) {
            restDownBean.setCreateEndDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.dateAdd(TimeUtil.parseAnyDate(restDownBean.getCreateEndDate()), TimeUtil.UNIT_DAY, 1)));
        }
        if (restDownBean != null && StringUtils.isNotBlank(restDownBean.getOvertimeEnd())) {
            restDownBean.setOvertimeEnd(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.dateAdd(TimeUtil.parseAnyDate(restDownBean.getOvertimeEnd()), TimeUtil.UNIT_DAY, 1)));
        }

        if (restDownBean != null && StringUtils.isNotBlank(restDownBean.getRestDateEnd())) {
            restDownBean.setRestDateEnd(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.dateAdd(TimeUtil.parseAnyDate(restDownBean.getRestDateEnd()), TimeUtil.UNIT_DAY, 1)));
        }
        return restDownDao.queryRestDown(restDownBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRestDown(RestDownBean restDownBean) throws Exception {
        restDownBean.setState("0");
        restDownBean.setExaminerAdv("0");
        restDownBean.setAuditorAdv("0");
        restDownBean.setApproverAdv("0");
        restDownBean.setCreateDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime()));
        if (restDownDao.addRestDown(restDownBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyRestDown(RestDownBean restDownBean) throws Exception {
        if (StringUtils.isBlank(restDownBean.getEmpId())) throw new MessageException("申请人不能为空!");
        if (StringUtils.isBlank(restDownBean.getOvertimeBeg())) throw new MessageException("加班开始时间不能为空!");
        if (StringUtils.isBlank(restDownBean.getOvertimeEnd())) throw new MessageException("加班结束时间不能为空!");
        if (StringUtils.isBlank(restDownBean.getRestDateBeg())) throw new MessageException("倒休结束时间不能为空!");
        if (StringUtils.isBlank(restDownBean.getRestDateEnd())) throw new MessageException("倒休结束时间不能为空!");
        if (StringUtils.isBlank(restDownBean.getExaminer())) throw new MessageException("审查人不能为空!");
        if (StringUtils.isBlank(restDownBean.getAuditor())) throw new MessageException("审核人不能为空!");

        if (StringUtils.isBlank(restDownBean.getReaId())) throw new MessageException("倒休原因不能为空!");
        List<JSONObject> list = new ArrayList<JSONObject>();
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDownBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        EmployeeBean employeeBean = employeeBeans.get(0);
        int level = 0;
        String posLeval = employeeBean.getPosLeval();
        if (StringUtils.isNotBlank(posLeval)) level = Integer.parseInt(posLeval);
        if (TimeUtil.differentDays(TimeUtil.parseAnyDate(restDownBean.getRestDateBeg()), TimeUtil.parseAnyDate(restDownBean.getRestDateEnd())) > differDay && level >= 3) {
            //超过3天
            if (StringUtils.isBlank(restDownBean.getApprover())) throw new MessageException("批准人不能为空！");
        }
        addRestDown(restDownBean);
        applyRestDownMsg(restDownBean, list, true);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }

    //申请倒休，修改倒休生成消息
    private void applyRestDownMsg(RestDownBean restDownBean, List<JSONObject> list, boolean flag) throws Exception {
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDownBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        List<RestReasonBean> restReasonBeans = restReasonService.queryRestReason(new RestReasonBean().setReaId(restDownBean.getReaId()));
        if (restReasonBeans.isEmpty()) throw new MessageException("未查询到该倒休原因!");
        RestReasonBean restReasonBean = restReasonBeans.get(0);
        EmployeeBean employeeBean = employeeBeans.get(0);
        JSONObject jsonObject = null;
        JSONObject data = null;
        if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
            //申请人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", employeeBean.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", flag ? "您已成功申请倒休,请等待审核!" : "您已成功修改倒休,请等待审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", restReasonBean.getReaName());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //请假时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(restDownBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", flag ? "您已成功申请倒休,请等待审核!" : "您已成功修改倒休,请等待审核!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }

        //审查人消息
        employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDownBean.getExaminer()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        EmployeeBean examiner = employeeBeans.get(0);
        if (StringUtils.isNotBlank(examiner.getOpenId())) {
            //审查人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", examiner.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            jsonObject.put("url", examineRestDownUrl.concat("?rdId=").concat(restDownBean.getRdId()));
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "有新的倒休申请需要审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", restReasonBean.getReaName());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //申请时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(restDownBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", "有新的倒休申请需要审核,点击查看详情!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRestDown(RestDownBean restDownBean) throws Exception {
        if (restDownDao.updateRestDown(restDownBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationRestDown(RestDownBean restDownBean) throws Exception {
        List<JSONObject> list = new ArrayList<JSONObject>();
        operation(restDownBean, list);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }

    //取消，修改，审核倒休
    private void operation(RestDownBean restDownBean, List<JSONObject> list) throws Exception {
        if (StringUtils.isBlank(restDownBean.getRdId())) throw new MessageException("倒休id不能为空!");
        List<RestDownBean> restDownBeans = queryRestDown(new RestDownBean().setRdId(restDownBean.getRdId()));
        if (restDownBeans.isEmpty()) throw new MessageException("未查询到该倒休!");
        RestDownBean restDown = restDownBeans.get(0);
        if ("1".equals(restDown.getState())) throw new MessageException("该倒休已被取消不能修改!");
        List<EmployeeBean> employeeBeans = null;
        JSONObject jsonObject = null;
        JSONObject data = null;
        if (StringUtils.isNotBlank(restDownBean.getState())) {
            //取消、修改倒休
            if ("1".equals(restDownBean.getState())) {
                //取消
                updateRestDown(new RestDownBean().setRdId(restDown.getRdId()).setState("1"));
            } else {
                //修改倒休，推送送消息
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDownBean.getEmpId()));
                if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
                EmployeeBean employeeBean = employeeBeans.get(0);
                int level = 0;
                String posLeval = employeeBean.getPosLeval();
                if (StringUtils.isNotBlank(posLeval)) level = Integer.parseInt(posLeval);
                if (TimeUtil.differentDays(TimeUtil.parseAnyDate(restDownBean.getRestDateBeg()), TimeUtil.parseAnyDate(restDownBean.getRestDateEnd())) > differDay && level >= 3) {
                    //超过3天
                    if (StringUtils.isBlank(restDownBean.getApprover())) throw new MessageException("批准人不能为空！");
                } else {
                    restDownDao.updateById(new RestDownBean().setApprover(null));
                }
                restDownBean.setState("0");
                restDownBean.setExaminerAdv("0");
                restDownBean.setAuditorAdv("0");
                restDownBean.setApproverAdv("0");
                updateRestDown(restDownBean);
                restDownBean.setCreateDate(restDown.getCreateDate());
                applyRestDownMsg(restDownBean, list, false);
            }
        } else if (StringUtils.isNotBlank(restDownBean.getExaminerAdv())) {
            //审查人审核倒休
            if (!"0".equals(restDown.getExaminerAdv())) throw new MessageException("已完成审核,不可重复审核!");
            if (!restDown.getExaminer().equals(restDownBean.getExaminer())) throw new MessageException("不可代替他人审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(restDownBean.getExaminerAdv())) {
                //不同意，通知申请人
                updateRestDown(new RestDownBean().setRdId(restDown.getRdId()).setExaminerAdv("2").setExaminerDate(nowStr).setState("4"));
                operationRestEmpMsg(restDown, list);
            } else {
                //同意，通知审核人
                updateRestDown(new RestDownBean().setRdId(restDown.getRdId()).setExaminerAdv("1").setExaminerDate(nowStr).setState("2"));
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDown.getAuditor()));
                if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                EmployeeBean auditor = employeeBeans.get(0);
                if (StringUtils.isNotBlank(auditor.getOpenId())) {
                    //审查人消息
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", auditor.getOpenId());
                    jsonObject.put("url", examineRestDownUrl.concat("?rdId=").concat(restDown.getRdId()));
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "有新的倒休申请需要审核!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", restDown.getEmpName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", restDown.getReaName());
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //请假时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(restDown.getCreateDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "有新的倒休申请需要审核,点击查看详情!");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            }
        } else if (StringUtils.isNotBlank(restDownBean.getAuditorAdv())) {
            //审核人审核倒休
            if (!"0".equals(restDown.getAuditorAdv())) throw new MessageException("已完成审核,不可重复审核!");
            if (!"1".equals(restDown.getExaminerAdv())) throw new MessageException("审查人未同意前不可审核!");
            if (!restDown.getAuditor().equals(restDownBean.getAuditor())) throw new MessageException("不可代替他人审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(restDownBean.getAuditorAdv())) {
                //不同意，通知申请人
                updateRestDown(new RestDownBean().setRdId(restDown.getRdId()).setAuditorAdv("2").setAuditorDate(nowStr).setState("4"));
                operationRestEmpMsg(restDown, list);
            } else {
                //同意，通知批准人
                RestDownBean bean = new RestDownBean().setRdId(restDown.getRdId()).setAuditorAdv("1").setAuditorDate(nowStr);
                bean.setState("2");
                if (StringUtils.isBlank(restDown.getApprover())) {
                    bean.setState("3");
                    updateRestDown(bean);
                    employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDown.getEmpId()));
                    if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
                    EmployeeBean employeeBean = employeeBeans.get(0);
                    if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                        jsonObject = new JSONObject();
                        jsonObject.put("touser", employeeBean.getOpenId());
                        jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                        data = new JSONObject();

                        JSONObject content = new JSONObject();
                        content.put("value", "您申请的倒休已通过审核!");
                        content.put("color", "#173177");
                        data.put("first", content);

                        //申请人
                        content = new JSONObject();
                        content.put("value", employeeBean.getEmpName());
                        content.put("color", "#173177");
                        data.put("keyword1", content);

                        //请假类型
                        content = new JSONObject();
                        content.put("value", restDown.getReaName());
                        content.put("color", "#173177");
                        data.put("keyword2", content);

                        //请假时间
                        content = new JSONObject();
                        content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(restDown.getCreateDate())));
                        content.put("color", "#173177");
                        data.put("keyword3", content);

                        //备注
                        content = new JSONObject();
                        content.put("value", "您申请的倒休已通过");
                        content.put("color", "#173177");
                        data.put("remark", content);

                        jsonObject.put("data", data);

                        list.add(jsonObject);
                    }
                } else {
                    updateRestDown(bean);
                    employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDown.getApprover()));
                    if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                    EmployeeBean auditor = employeeBeans.get(0);
                    if (StringUtils.isNotBlank(auditor.getOpenId())) {
                        //审查人消息
                        jsonObject = new JSONObject();
                        jsonObject.put("touser", auditor.getOpenId());
                        jsonObject.put("url", examineRestDownUrl.concat("?rdId=").concat(restDown.getRdId()));
                        jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                        data = new JSONObject();

                        JSONObject content = new JSONObject();
                        content.put("value", "有新的倒休申请需要审核!");
                        content.put("color", "#173177");
                        data.put("first", content);

                        //申请人
                        content = new JSONObject();
                        content.put("value", restDown.getEmpName());
                        content.put("color", "#173177");
                        data.put("keyword1", content);

                        //请假类型
                        content = new JSONObject();
                        content.put("value", restDown.getReaName());
                        content.put("color", "#173177");
                        data.put("keyword2", content);

                        //请假时间
                        content = new JSONObject();
                        content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(restDown.getCreateDate())));
                        content.put("color", "#173177");
                        data.put("keyword3", content);

                        //备注
                        content = new JSONObject();
                        content.put("value", "有新的倒休申请需要审核,点击查看详情!");
                        content.put("color", "#173177");
                        data.put("remark", content);

                        jsonObject.put("data", data);

                        list.add(jsonObject);
                    }
                }
            }
        } else if (StringUtils.isNotBlank(restDownBean.getApproverAdv())) {
            //批准人审核
            if (!"0".equals(restDown.getApproverAdv())) throw new MessageException("该申请已完成审核,不可重复审核!");
            if (!"1".equals(restDown.getAuditorAdv())) throw new MessageException("审核人未同意前不可审核!");
            if (StringUtils.isBlank(restDown.getApprover())) throw new MessageException("该申请只有2个审核人不可审核!");
            if (!restDown.getApprover().equals(restDownBean.getApprover())) throw new MessageException("不可代替他人审核!");

            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(restDownBean.getApproverAdv())) {
                //未通过审核，通知申请人
                updateRestDown(new RestDownBean().setRdId(restDown.getRdId()).setApproverAdv("2").setApproverDate(nowStr));
                operationRestEmpMsg(restDown, list);
            } else {
                //通过审核,通知申请人
                updateRestDown(new RestDownBean().setRdId(restDown.getRdId()).setApproverAdv("1").setApproverDate(nowStr));

                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDown.getEmpId()));
                if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
                EmployeeBean employeeBean = employeeBeans.get(0);
                if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", employeeBean.getOpenId());
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "您申请的倒休已通过审核!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", employeeBean.getEmpName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", restDown.getReaName());
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //请假时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(restDown.getCreateDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "您申请的倒休已通过");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            }
        }
    }

    //不同意申请人消息
    private void operationRestEmpMsg(RestDownBean restDownBean, List<JSONObject> list) throws Exception {
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(restDownBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        List<RestReasonBean> restReasonBeans = restReasonService.queryRestReason(new RestReasonBean().setReaId(restDownBean.getReaId()));
        if (restReasonBeans.isEmpty()) throw new MessageException("未查询到该倒休原因!");
        RestReasonBean restReasonBean = restReasonBeans.get(0);
        EmployeeBean employeeBean = employeeBeans.get(0);
        JSONObject jsonObject = null;
        if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
            //申请人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", employeeBean.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            jsonObject.put("url", modifyRestDownUrl.concat("?rdId=").concat(restDownBean.getRdId()));
            JSONObject data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "倒休审核未通过!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", restReasonBean.getReaName());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //请假时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(restDownBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", "您申请的倒休未通过审核，点击查看详情!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
    }

    @Override
    public List<RestDownBean> queryUntreatedRestDown(RestDownBean restDownBean) throws Exception {
        return restDownDao.queryUntreatedRestDown(restDownBean);
    }
}
