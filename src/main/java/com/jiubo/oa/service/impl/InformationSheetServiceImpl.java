package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.bean.InformationSheetBean;
import com.jiubo.oa.dao.InformationSheetDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmployeeService;
import com.jiubo.oa.service.InformationSheetService;
import com.jiubo.oa.service.WxSendMessageService;
import com.jiubo.oa.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InformationSheetServiceImpl extends ServiceImpl<InformationSheetDao, InformationSheetBean> implements InformationSheetService {


    @Autowired
    InformationSheetDao informationSheetDao;

    @Autowired
    WxSendMessageService wxSendMessageService;

    @Autowired
    EmployeeService employeeService;

    //信息申请单审核界面
    @Value("${modifyInformationSheetUrl}")
    private String modifyInformationSheetUrl;

    //信息申请单审核界面
    @Value("${examineInformationSheetUrl}")
    private String examineInformationSheetUrl;


    @Override
    public List<InformationSheetBean> queryInformationSheet(InformationSheetBean informationSheetBean) throws Exception {
        return informationSheetDao.queryInformationSheet(informationSheetBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addInformationSheet(InformationSheetBean informationSheetBean) throws Exception {
        informationSheetBean.setState("0");
        informationSheetBean.setSendDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime()));
        if (informationSheetDao.addInformationSheet(informationSheetBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInformationSheet(InformationSheetBean informationSheetBean) throws Exception {


        if (informationSheetDao.updateInformationSheet(informationSheetBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyInformationSheet(InformationSheetBean informationSheetBean) throws Exception {
        if (StringUtils.isBlank(informationSheetBean.getSheetType())) throw new MessageException("信息单类型不能为空");
        if (StringUtils.isBlank(informationSheetBean.getDegreeType())) throw new MessageException("信息单重要程度不能为空");
        if (StringUtils.isBlank(informationSheetBean.getDeptSendId())) throw new MessageException("信息单发起部门不能为空");
        if (StringUtils.isBlank(informationSheetBean.getDeptRecId())) throw new MessageException("信息单接收部门不能为空");
        if (StringUtils.isBlank(informationSheetBean.getTheme())) throw new MessageException("信息单主题不能为空");
        if (StringUtils.isBlank(informationSheetBean.getContent())) throw new MessageException("信息单内容不能为空");

        List<JSONObject> list = new ArrayList<JSONObject>();
        addInformationSheet(informationSheetBean);
        applyInformationSheet(informationSheetBean, list, true);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }


    //申请信息单，修改信息单生成消息
    private void applyInformationSheet(InformationSheetBean informationSheetBean, List<JSONObject> list, boolean flag) throws Exception {
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(informationSheetBean.getSenderId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        EmployeeBean applyer = employeeBeans.get(0);
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
            content.put("value", flag ? "您已成功申请信息传递单,请等待审核!" : "您已成功修改信息传递单,请等待审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //申请主题
            content = new JSONObject();
            content.put("value", informationSheetBean.getTheme());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //信息单申请时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(informationSheetBean.getSendDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", flag ? "您已成功信息传递单信息传递单,请等待审核!" : "您已成功修改信息传递单,请等待审核!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }

        employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(informationSheetBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        employeeBean = employeeBeans.get(0);
        if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
            //申请人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", employeeBean.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", flag ? "有一条信息传递单正在申请!" : "有一条信息传递单正在申请!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", applyer.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //申请主题
            content = new JSONObject();
            content.put("value", informationSheetBean.getTheme());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //信息单申请时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(informationSheetBean.getSendDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", flag ? "您已成功信息传递单信息传递单,请等待审核!" : "您已成功修改信息传递单,请等待审核!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }

        //审查人消息
        employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(informationSheetBean.getEmpSendId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        EmployeeBean examiner = employeeBeans.get(0);
        if (StringUtils.isNotBlank(examiner.getOpenId())) {
            //审查人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", examiner.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            jsonObject.put("url", examineInformationSheetUrl.concat("?infId=").concat(informationSheetBean.getInfId()));
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "有新的信息传递单需要审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", applyer.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //信息单主题
            content = new JSONObject();
            content.put("value", informationSheetBean.getTheme());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //申请时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(informationSheetBean.getSendDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", "有新的信息传递单需要审核,点击查看详情!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationInformationSheet(InformationSheetBean informationSheetBean) throws Exception {
        List<JSONObject> list = new ArrayList<JSONObject>();
        operation(informationSheetBean, list);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }

    //信息单 1-取消、修改、审核
    private void operation(InformationSheetBean informationSheetBean, List<JSONObject> list) throws Exception {
        if (StringUtils.isBlank(informationSheetBean.getInfId())) throw new MessageException("id不能为空!");
        List<InformationSheetBean> informationSheetBeans = queryInformationSheet(new InformationSheetBean().setInfId(informationSheetBean.getInfId()));
        if (informationSheetBeans.isEmpty()) throw new MessageException("未查询到该信息传递单申请!");
        InformationSheetBean informationSheetBean1 = informationSheetBeans.get(0);
        List<EmployeeBean> employeeBeans = null;
        JSONObject jsonObject = null;
        JSONObject data = null;
        if ("1".equals(informationSheetBean1.getState())) throw new MessageException("该信息传递单申请已被取消!");
        if (StringUtils.isNotBlank(informationSheetBean.getState())) {
            if ("1".equals(informationSheetBean.getState())) {
                //取消信息单
                updateInformationSheet(new InformationSheetBean().setInfId(informationSheetBean.getInfId()).setState("1"));
            } else {
                //修改,通知申请人+审查人
                informationSheetBean.setState("2");
                if (StringUtils.isBlank(informationSheetBean.getInfId())) throw new MessageException("信息单ID不能为空");
                if (StringUtils.isBlank(informationSheetBean.getSheetType())) throw new MessageException("信息单类型不能为空");
                if (StringUtils.isBlank(informationSheetBean.getDegreeType())) throw new MessageException("信息单重要程度不能为空");
                if (StringUtils.isBlank(informationSheetBean.getDeptSendId())) throw new MessageException("信息单发起部门不能为空");
                if (StringUtils.isBlank(informationSheetBean.getDeptRecId())) throw new MessageException("信息单接收部门不能为空");
                if (StringUtils.isBlank(informationSheetBean.getTheme())) throw new MessageException("信息单主题不能为空");
                if (StringUtils.isBlank(informationSheetBean.getContent())) throw new MessageException("信息单内容不能为空");
                informationSheetBean.setSendDate(informationSheetBean1.getSendDate());
                System.out.println("informationSheetBean:"+informationSheetBean.toString());
                updateInformationSheet(informationSheetBean);
                applyInformationSheet(informationSheetBean, list, false);
            }
        }else if (StringUtils.isNotBlank(informationSheetBean.getSendAgree())) {
            //发起部门负责人审核
            if (!"0".equals(informationSheetBean1.getSendAgree())) throw new MessageException("已完成审核,不可重复审核!");
            if (!informationSheetBean.getEmpSendId().equals(informationSheetBean1.getEmpSendId()))
                throw new MessageException("不可代替他人审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(informationSheetBean.getSendAgree())) {
                //不同意，发消息给申请人
                updateInformationSheet(new InformationSheetBean().setInfId(informationSheetBean.getInfId()).setSendAgree("2").setSendContent(informationSheetBean.getSendContent()).setSendAppDate(nowStr).setState("4"));
                operationInformationSheetEmpMsg(informationSheetBean, list);
            } else {
                //发起部门负责人同意，通知接收部门负责人
                updateInformationSheet(new InformationSheetBean().setInfId(informationSheetBean.getInfId()).setSendAgree("1").setSendContent(informationSheetBean.getSendContent()).setSendAppDate(nowStr).setState("2"));

                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(informationSheetBean.getEmpRecId()));
                if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                EmployeeBean auditor = employeeBeans.get(0);
                if (StringUtils.isNotBlank(auditor.getOpenId())) {
                    //审核人消息
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", auditor.getOpenId());
                    jsonObject.put("url", modifyInformationSheetUrl.concat("?infId=").concat(informationSheetBean.getInfId()));
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "有新的信息传递单需要审核!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", informationSheetBean.getSenderName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", informationSheetBean.getTheme());
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //请假时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(informationSheetBean.getSendDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "有新的信息传递单需要审核,点击查看详情!");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            }
        } else if (StringUtils.isNotBlank(informationSheetBean.getRecAgree())) {
            //信息单接收部门审核
            if (!"0".equals(informationSheetBean1.getRecAgree())) throw new MessageException("已完成审核,不可重复审核!");
            if (!informationSheetBean.getEmpRecId().equals(informationSheetBean1.getEmpRecId()))
                throw new MessageException("不可代替他人审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(informationSheetBean.getRecAgree())) {
                //不同意，发消息给申请人
                updateInformationSheet(new InformationSheetBean().setInfId(informationSheetBean.getInfId()).setRecAgree("2").setRecContent(informationSheetBean.getRecContent()).setRecAppDate(nowStr).setState("4"));
                operationInformationSheetEmpMsg(informationSheetBean, list);
            } else {
                //同意，通知接收部门负责人审核
                updateInformationSheet(new InformationSheetBean().setInfId(informationSheetBean.getInfId()).setRecAgree("1").setRecContent(informationSheetBean.getRecContent()).setRecAppDate(nowStr).setState("3"));
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(informationSheetBean.getEmpRecId()));
                if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                EmployeeBean auditor = employeeBeans.get(0);
                if (StringUtils.isNotBlank(auditor.getOpenId())) {
                    //审核人消息
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", auditor.getOpenId());
                    jsonObject.put("url", modifyInformationSheetUrl.concat("?infId=").concat(informationSheetBean.getInfId()));
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "有新的信息传递单需要审核!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", informationSheetBean.getSenderName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //信息单主题
                    content = new JSONObject();
                    content.put("value", informationSheetBean.getTheme());
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //信息单发起时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(informationSheetBean.getSendDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "有新的信息传递单需要审核,点击查看详情!");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            }
        }

    }

    //不同意申请人消息
    private void operationInformationSheetEmpMsg(InformationSheetBean informationSheetBean, List<JSONObject> list) throws Exception {
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(informationSheetBean.getSenderId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        EmployeeBean employeeBean = employeeBeans.get(0);
        JSONObject jsonObject = null;
        if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
            //申请人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", employeeBean.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            jsonObject.put("url", modifyInformationSheetUrl.concat("?infId=").concat(informationSheetBean.getInfId()));
            JSONObject data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "信息传递单审核未通过!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //信息单主题
            content = new JSONObject();
            content.put("value", informationSheetBean.getTheme());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //信息单审核时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(informationSheetBean.getSendAppDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", "您申请的信息传递单未通过审核，点击查看详情!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
    }


}
