package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSON;
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
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

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
        if (StringUtils.isBlank(askLeaveBean.getApprover())) throw new MessageException("批准人不能为空！");
        if (StringUtils.isBlank(askLeaveBean.getLtId())) throw new MessageException("假期类型不能为空!");
        askLeaveBean.setState("0");
        askLeaveBean.setExaminerAdv("0");
        askLeaveBean.setExaminerAdv("0");
        askLeaveBean.setAuditorAdv("0");
        askLeaveBean.setApproverAdv("0");
        addAskLeave(askLeaveBean);
        applyLeaveSendMsg(askLeaveBean, list);
    }

    //申请假期发送消息
    private void applyLeaveSendMsg(AskLeaveBean askLeaveBean, List<JSONObject> list) throws Exception {
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
            jsonObject.put("template_id", "YgnW7CB6Ch57goXpk3k6D2O4967OtcZ60fBp6YjbKKQ");
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "您已成功申请" + leaveTypeBean.getLtName() + ",请等待审核!");
            content.put("color", "#173177");
            data.put("first", content);

            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            content = new JSONObject();
            content.put("value", askLeaveBean.getBegDate() + "--" + askLeaveBean.getEndDate());
            content.put("color", "#173177");
            data.put("keyword2", content);

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
            jsonObject.put("template_id", "Ask5LJTINAHjGJPnkk8r46y0Yd8BnS1eauPlifoMf0s");
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "有新的假期申请需要审核!");
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

            //开始时间
            content = new JSONObject();
            content.put("value", askLeaveBean.getBegDate());
            content.put("color", "#173177");
            data.put("keyword3", content);

            //结束时间
            content = new JSONObject();
            content.put("value", askLeaveBean.getEndDate());
            content.put("color", "#173177");
            data.put("keyword4", content);

            //备注
            content = new JSONObject();
            content.put("value", askLeaveBean.getRemark());
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
        updateLeave(askLeaveBean,list);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
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
                askLeaveBean.setState("0");
                askLeaveBean.setExaminerAdv("0");
                askLeaveBean.setExaminerAdv("0");
                askLeaveBean.setAuditorAdv("0");
                askLeaveBean.setApproverAdv("0");
                updateAskLeave(askLeaveBean);
                applyLeaveSendMsg(askLeaveBean, list);
            }
        } else if (StringUtils.isNotBlank(askLeaveBean.getExaminerAdv())) {
            //审查人审核
            if (!"0".equals(askLeave.getExaminerAdv())) throw new MessageException("该申请已完成审核,不可重复审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(askLeaveBean.getExaminerAdv())) {
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setExaminerAdv("2").setExaminerDate(nowStr));
                //通知申请人，审核未通过
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getEmpId()));
                if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
                EmployeeBean employeeBean = employeeBeans.get(0);
                if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", employeeBean.getOpenId());
                    jsonObject.put("template_id", "k4Cca45hu5DUriXAff3v3mnrIX6Yaf4IjateuLAkxgk");
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "您的请假申请未通过审核!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", askLeave.getLtName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假时间
                    content = new JSONObject();
                    content.put("value", askLeave.getBegDate() + "--" + askLeave.getEndDate());
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //请假原因
                    content = new JSONObject();
                    content.put("value", askLeave.getRemark());
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    content = new JSONObject();
                    content.put("value", askLeave.getRemark());
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //审批人
                    content = new JSONObject();
                    content.put("value", askLeave.getExaminerName());
                    content.put("color", "#173177");
                    data.put("keyword4", content);

                    //审批结果
                    content = new JSONObject();
                    content.put("value", "未通过!");
                    content.put("color", "#173177");
                    data.put("keyword5", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            } else {
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setExaminerAdv("1").setExaminerDate(nowStr));
                //通知审核人审批申请
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getAuditor()));
                if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                EmployeeBean auditor = employeeBeans.get(0);
                if (StringUtils.isNotBlank(auditor.getOpenId())) {
                    //审查人消息
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", auditor.getOpenId());
                    jsonObject.put("template_id", "Ask5LJTINAHjGJPnkk8r46y0Yd8BnS1eauPlifoMf0s");
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "有新的假期申请需要审核!");
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

                    //开始时间
                    content = new JSONObject();
                    content.put("value", askLeaveBean.getBegDate());
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //结束时间
                    content = new JSONObject();
                    content.put("value", askLeaveBean.getEndDate());
                    content.put("color", "#173177");
                    data.put("keyword4", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", askLeaveBean.getRemark());
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            }
        } else if (StringUtils.isNotBlank(askLeaveBean.getApproverAdv())) {
            //批准人审核
            if (!"0".equals(askLeave.getApproverAdv())) throw new MessageException("该申请已完成审核,不可重复审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            String result = "未通过";
            if ("2".equals(askLeaveBean.getApproverAdv())) {
                //未通过审核，通知申请人
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setApproverAdv("2").setApproverDate(nowStr));
            } else {
                result = "已通过";
                //通过审核
                updateAskLeave(new AskLeaveBean().setAlId(askLeave.getAlId()).setApproverAdv("1").setApproverDate(nowStr));
                //通知申请人
            }


            //通知申请人
            employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(askLeave.getEmpId()));
            if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
            EmployeeBean employeeBean = employeeBeans.get(0);
            if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                jsonObject = new JSONObject();
                jsonObject.put("touser", employeeBean.getOpenId());
                jsonObject.put("template_id", "k4Cca45hu5DUriXAff3v3mnrIX6Yaf4IjateuLAkxgk");
                data = new JSONObject();

                JSONObject content = new JSONObject();
                content.put("value", "您的请假申请" + result + "审核!");
                content.put("color", "#173177");
                data.put("first", content);

                //请假类型
                content = new JSONObject();
                content.put("value", askLeave.getLtName());
                content.put("color", "#173177");
                data.put("keyword1", content);

                //请假时间
                content = new JSONObject();
                content.put("value", askLeave.getBegDate() + "--" + askLeave.getEndDate());
                content.put("color", "#173177");
                data.put("keyword2", content);

                //请假原因
                content = new JSONObject();
                content.put("value", askLeave.getRemark());
                content.put("color", "#173177");
                data.put("keyword3", content);

                content = new JSONObject();
                content.put("value", askLeave.getRemark());
                content.put("color", "#173177");
                data.put("keyword3", content);

                //审批人
                content = new JSONObject();
                content.put("value", askLeave.getApproverName());
                content.put("color", "#173177");
                data.put("keyword4", content);

                //审批结果
                content = new JSONObject();
                content.put("value", result);
                content.put("color", "#173177");
                data.put("keyword5", content);

                jsonObject.put("data", data);

                list.add(jsonObject);
            }
        }
    }
}
