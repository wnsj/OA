package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.AskLeaveBean;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.bean.LeaveTypeBean;
import com.jiubo.oa.dao.AskLeaveDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.AskLeaveService;
import com.jiubo.oa.service.EmployeeService;
import com.jiubo.oa.service.LeaveTypeService;
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
 * @desc:请假服务实现类
 * @date: 2020-01-02 16:55
 * @author: dx
 * @version: 1.0
 */
@Service
public class AskLeaveServiceImpl extends ServiceImpl<AskLeaveDao, AskLeaveBean> implements AskLeaveService {

    //相差天数
    @Value("${differDay}")
    private int differDay;

    //修改页面
    @Value("${modifyUrl}")
    private String modifyUrl;

    //申请页面
    @Value("${examineUrl}")
    private String examineUrl;

    @Autowired
    private AskLeaveDao askLeaveDao;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    private WxSendMessageService wxSendMessageService;

    @Override
    public List<AskLeaveBean> queryAskLeave(AskLeaveBean askLeaveBean) throws Exception {
        if (askLeaveBean != null && StringUtils.isNotBlank(askLeaveBean.getCreateEndDate())) {
            askLeaveBean.setCreateEndDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.dateAdd(TimeUtil.parseAnyDate(askLeaveBean.getCreateEndDate()), TimeUtil.UNIT_DAY, 1)));
        }
        if (askLeaveBean != null && StringUtils.isNotBlank(askLeaveBean.getQEndDate())) {
            askLeaveBean.setQEndDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.dateAdd(TimeUtil.parseAnyDate(askLeaveBean.getQEndDate()), TimeUtil.UNIT_DAY, 1)));
        }
        return askLeaveDao.queryAskLeave(askLeaveBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAskLeave(AskLeaveBean askLeaveBean) throws Exception {
        askLeaveBean.setState("0");
        askLeaveBean.setExaminerAdv("0");
        askLeaveBean.setAuditorAdv("0");
        askLeaveBean.setApproverAdv("0");
        askLeaveBean.setCreateDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime()));
        if (askLeaveDao.addAskLeave(askLeaveBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAskLeave(AskLeaveBean askLeaveBean) throws Exception {
        if (askLeaveDao.updateAskLeave(askLeaveBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyLeave(AskLeaveBean askLeaveBean) throws Exception {
        List<JSONObject> list = new ArrayList<JSONObject>();
        addLeave(askLeaveBean, list);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }

    //申请假期
    private void addLeave(AskLeaveBean askLeaveBean, List<JSONObject> list) throws Exception {
        if (StringUtils.isBlank(askLeaveBean.getEmpId())) throw new MessageException("申请人不能为空!");
        if (StringUtils.isBlank(askLeaveBean.getBegDate())) throw new MessageException("请假开始时间不能为空!");
        if (StringUtils.isBlank(askLeaveBean.getEndDate())) throw new MessageException("请假结束时间不能为空!");
        if (StringUtils.isBlank(askLeaveBean.getExaminer())) throw new MessageException("审查人不能为空!");
        if (StringUtils.isBlank(askLeaveBean.getAuditor())) throw new MessageException("审核人不能为空!");

        if (StringUtils.isBlank(askLeaveBean.getLtId())) throw new MessageException("假期类型不能为空!");
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeaveBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("申请人工号错误!");
        EmployeeBean employeeBean = employeeBeans.get(0);

        if (TimeUtil.differentDays(TimeUtil.parseAnyDate(askLeaveBean.getBegDate()), TimeUtil.parseAnyDate(askLeaveBean.getEndDate())) > differDay
                && "3".equals(employeeBean.getPosLeval())) {
            //超过3天
            if (StringUtils.isBlank(askLeaveBean.getApprover())) throw new MessageException("批准人不能为空！");
        }
        askLeaveBean.setState("0");
        askLeaveBean.setExaminerAdv("0");
        askLeaveBean.setApproverAdv("0");
        addAskLeave(askLeaveBean);
        applyLeaveSendMsg(askLeaveBean, true, list);
    }

    //申请假期发送消息
    private void applyLeaveSendMsg(AskLeaveBean askLeaveBean, boolean flag, List<JSONObject> list) throws Exception {
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeaveBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("申请人工号错误!");
        EmployeeBean employeeBean = employeeBeans.get(0);

        List<LeaveTypeBean> leaveTypeBeans = leaveTypeService.queryLeaveType(new LeaveTypeBean().setLtId(askLeaveBean.getLtId()));
        if (leaveTypeBeans.isEmpty()) throw new MessageException("请假类型错误!");
        LeaveTypeBean leaveTypeBean = leaveTypeBeans.get(0);
        JSONObject jsonObject = null;
        JSONObject data = null;
        if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
            //申请人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", employeeBean.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            //if (!flag) jsonObject.put("url", modifyUrl.concat("?alId=").concat(askLeaveBean.getAlId()));
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", flag ? "您已成功申请" + leaveTypeBean.getLtName() + ",请等待审核!" : "您已成功修改请假申请,请等待审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", leaveTypeBean.getLtName());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //请假时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(askLeaveBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", flag ? "您已成功申请" + leaveTypeBean.getLtName() + ",请等待审核!" : "您已成功修改请假申请,请等待审核!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
        employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeaveBean.getExaminer()));
        if (employeeBeans.isEmpty()) throw new MessageException("审查人工号错误!");
        EmployeeBean examiner = employeeBeans.get(0);
        if (StringUtils.isNotBlank(examiner.getOpenId())) {
            //审查人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", examiner.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            jsonObject.put("url", examineUrl.concat("?alId=").concat(askLeaveBean.getAlId()));
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "有新的请假申请需要审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", leaveTypeBean.getLtName());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //申请时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(askLeaveBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", "有新的请假申请需要审核,点击查看详情!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationLeave(AskLeaveBean askLeaveBean) throws Exception {
        List<JSONObject> list = new ArrayList<>();
        updateLeave(askLeaveBean, list);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }

    @Override
    public List<AskLeaveBean> queryUntreatedAskLeave(AskLeaveBean askLeaveBean) {
        return askLeaveDao.queryUntreatedAskLeave(askLeaveBean);
    }

    @Override
    public List<AskLeaveBean> queryUntreatedApply(AskLeaveBean askLeaveBean) throws Exception {
        return askLeaveDao.queryUntreatedApply(askLeaveBean);
    }

    //取消，审核申请
    private void updateLeave(AskLeaveBean askLeaveBean, List<JSONObject> list) throws Exception {
        if (StringUtils.isBlank(askLeaveBean.getAlId())) throw new MessageException("假期id为空!");
        List<AskLeaveBean> askLeaveBeans = queryAskLeave(new AskLeaveBean().setAlId(askLeaveBean.getAlId()));
        if (askLeaveBeans.isEmpty()) throw new MessageException("假期id错误!");
        AskLeaveBean askLeave = askLeaveBeans.get(0);
        JSONObject jsonObject = null;
        JSONObject data = null;
        List<EmployeeBean> employeeBeans = null;
        if ("1".equals(askLeave.getState())) throw new MessageException("该假期已被取消不可修改!");
        //判断是否是本人取消
        if (StringUtils.isNotBlank(askLeaveBean.getState())) {
            if ("1".equals(askLeave.getApproverAdv())) throw new MessageException("该假期已完成审核不可修改!");

            if ("1".equals(askLeaveBean.getState())) {
                //取消申请
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setState("1"));
            } else {
                //修改申请，则申请需重新审核
                if (TimeUtil.differentDays(TimeUtil.parseAnyDate(askLeaveBean.getBegDate()), TimeUtil.parseAnyDate(askLeaveBean.getEndDate())) > differDay) {
                    //超过3天
                    if (StringUtils.isBlank(askLeaveBean.getApprover())) throw new MessageException("批准人不能为空！");
                } else {

                    askLeaveDao.updateById(new AskLeaveBean().setApprover(null));
                }
                askLeaveBean.setState("0");
                askLeaveBean.setExaminerAdv("0");
                askLeaveBean.setExaminerAdv("0");
                askLeaveBean.setAuditorAdv("0");
                askLeaveBean.setApproverAdv("0");
                askLeaveBean.setCreateDate(askLeave.getCreateDate());
                askLeaveBean.setState("2");
                updateAskLeave(askLeaveBean);
                applyLeaveSendMsg(askLeaveBean, false, list);
            }
        } else if (StringUtils.isNotBlank(askLeaveBean.getExaminerAdv())) {
            //审查人审核
            if (!"0".equals(askLeave.getExaminerAdv())) throw new MessageException("该申请已完成审核,不可重复审核!");
            if (!askLeave.getExaminer().equals(askLeaveBean.getExaminer())) throw new MessageException("不可代替他人审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(askLeaveBean.getExaminerAdv())) {
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setExaminerAdv("2").setExaminerDate(nowStr).setState("4"));
                //通知申请人，审核未通过
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getEmpId()));
                if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
                EmployeeBean employeeBean = employeeBeans.get(0);
                if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", employeeBean.getOpenId());
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    jsonObject.put("url", modifyUrl.concat("?alId=").concat(askLeave.getAlId()));
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "审核未通过!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", employeeBean.getEmpName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", askLeave.getLtName());
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //请假时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(askLeave.getCreateDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "审核未通过，点击查看详情!");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            } else {
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setExaminerAdv("1").setExaminerDate(nowStr).setState("2"));
                if (StringUtils.isBlank(askLeave.getApprover())) {
                    //通知申请人审核通过
                    employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getEmpId()));
                    if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
                    EmployeeBean employeeBean = employeeBeans.get(0);
                    if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                        jsonObject = new JSONObject();
                        jsonObject.put("touser", employeeBean.getOpenId());
                        jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                        if ("2".equals(askLeaveBean.getApproverAdv()))
                            jsonObject.put("url", modifyUrl.concat("?alId=").concat(askLeave.getAlId()));
                        data = new JSONObject();

                        JSONObject content = new JSONObject();
                        content.put("value", "您申请的请假申请已通过审核!");
                        content.put("color", "#173177");
                        data.put("first", content);

                        //申请人
                        content = new JSONObject();
                        content.put("value", employeeBean.getEmpName());
                        content.put("color", "#173177");
                        data.put("keyword1", content);

                        //请假类型
                        content = new JSONObject();
                        content.put("value", askLeave.getLtName());
                        content.put("color", "#173177");
                        data.put("keyword2", content);

                        //请假时间
                        content = new JSONObject();
                        content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(askLeave.getCreateDate())));
                        content.put("color", "#173177");
                        data.put("keyword3", content);

                        //备注
                        content = new JSONObject();
                        content.put("value", "您申请的请假申请已通过");
                        content.put("color", "#173177");
                        data.put("remark", content);

                        jsonObject.put("data", data);

                        list.add(jsonObject);
                    }
                } else {
                    //通知审核人审批申请
                    employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getAuditor()));
                    if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                    EmployeeBean auditor = employeeBeans.get(0);
                    if (StringUtils.isNotBlank(auditor.getOpenId())) {
                        //审查人消息
                        jsonObject = new JSONObject();
                        jsonObject.put("touser", auditor.getOpenId());
                        jsonObject.put("url", examineUrl.concat("?alId=").concat(askLeaveBean.getAlId()));
                        jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                        data = new JSONObject();

                        JSONObject content = new JSONObject();
                        content.put("value", "有新的请假申请需要审核!");
                        content.put("color", "#173177");
                        data.put("first", content);

                        //申请人
                        content = new JSONObject();
                        content.put("value", auditor.getEmpName());
                        content.put("color", "#173177");
                        data.put("keyword1", content);

                        //请假类型
                        content = new JSONObject();
                        content.put("value", askLeave.getLtName());
                        content.put("color", "#173177");
                        data.put("keyword2", content);

                        //请假时间
                        content = new JSONObject();
                        content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(askLeave.getCreateDate())));
                        content.put("color", "#173177");
                        data.put("keyword3", content);

                        //备注
                        content = new JSONObject();
                        content.put("value", "有新的请假申请需要审核,点击查看详情!");
                        content.put("color", "#173177");
                        data.put("remark", content);

                        jsonObject.put("data", data);

                        list.add(jsonObject);
                    }
                }
            }
        } else if (StringUtils.isNotBlank(askLeaveBean.getAuditorAdv())) {
            //审核人审核
            if (!"0".equals(askLeave.getAuditorAdv())) throw new MessageException("该申请已完成审核,不可重复审核!");
            if (!"1".equals(askLeave.getExaminerAdv())) throw new MessageException("审查人未同意前不可审核!");
            if (!askLeave.getAuditor().equals(askLeaveBean.getAuditor())) throw new MessageException("不可代替他人审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(askLeaveBean.getAuditorAdv())) {
                //审核未通过，通知申请人
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setAuditorAdv("2").setAuditorDate(nowStr).setState("4"));
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getEmpId()));
                if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
                EmployeeBean employeeBean = employeeBeans.get(0);
                if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", employeeBean.getOpenId());
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    jsonObject.put("url", modifyUrl.concat("?alId=").concat(askLeave.getAlId()));
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "审核未通过!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", employeeBean.getEmpName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", askLeave.getLtName());
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //请假时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(askLeave.getCreateDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "审核未通过，点击查看详情!");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            } else {
                //审核通过，通知批准人
                if (StringUtils.isBlank(askLeave.getApprover())||"0".equals(askLeave.getApprover()))
                    updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setAuditorAdv("1").setAuditorDate(nowStr).setState("3"));
                else updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setAuditorAdv("1").setAuditorDate(nowStr).setState("2"));
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getApprover()));
                if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                EmployeeBean approver = employeeBeans.get(0);
                if (StringUtils.isNotBlank(approver.getOpenId())) {
                    //审查人消息
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", approver.getOpenId());
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    jsonObject.put("url", examineUrl.concat("?alId=").concat(askLeaveBean.getAlId()));
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "有新的请假申请需要审核!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", approver.getEmpName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", askLeave.getLtName());
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //开始时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(askLeave.getCreateDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "有新的请假申请需要审核,点击查看详情!");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            }
        } else if (StringUtils.isNotBlank(askLeaveBean.getApproverAdv())) {
            //批准人审核
            if (!"0".equals(askLeave.getApproverAdv())) throw new MessageException("该申请已完成审核,不可重复审核!");
            if (!"1".equals(askLeave.getAuditorAdv())) throw new MessageException("审核人未同意前不可审核!");
            if (StringUtils.isBlank(askLeave.getApprover()))throw new MessageException("该申请只有2个审核人不可审核!");
            if (!askLeave.getApprover().equals(askLeaveBean.getApprover())) throw new MessageException("不可代替他人审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            String result = "未通过";
            if ("2".equals(askLeaveBean.getApproverAdv())) {
                //未通过审核，通知申请人
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setApproverAdv("2").setApproverDate(nowStr).setState("4"));
            } else {
                result = "已通过";
                //通过审核,通知申请人
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setApproverAdv("1").setApproverDate(nowStr).setState("3"));
            }

            employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getEmpId()));
            if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
            EmployeeBean employeeBean = employeeBeans.get(0);
            if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                jsonObject = new JSONObject();
                jsonObject.put("touser", employeeBean.getOpenId());
                jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                if ("2".equals(askLeaveBean.getApproverAdv()))
                    jsonObject.put("url", modifyUrl.concat("?alId=").concat(askLeave.getAlId()));
                data = new JSONObject();

                JSONObject content = new JSONObject();
                content.put("value", "您申请的请假申请" + result + "审核!");
                content.put("color", "#173177");
                data.put("first", content);

                //申请人
                content = new JSONObject();
                content.put("value", employeeBean.getEmpName());
                content.put("color", "#173177");
                data.put("keyword1", content);

                //请假类型
                content = new JSONObject();
                content.put("value", askLeave.getLtName());
                content.put("color", "#173177");
                data.put("keyword2", content);

                //请假时间
                content = new JSONObject();
                content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(askLeave.getCreateDate())));
                content.put("color", "#173177");
                data.put("keyword3", content);

                //备注
                content = new JSONObject();
                content.put("value", "2".equals(askLeaveBean.getApproverAdv()) ? "您申请的请假申请审核未通过，点击查看详情!" : "您申请的请假申请已通过");
                content.put("color", "#173177");
                data.put("remark", content);

                jsonObject.put("data", data);

                list.add(jsonObject);
            }
        }
    }
}
