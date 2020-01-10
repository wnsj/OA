package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.bean.ForgetCardBean;
import com.jiubo.oa.bean.ForgetCardReasonBean;
import com.jiubo.oa.bean.RestReasonBean;
import com.jiubo.oa.dao.ForgetCardDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmployeeService;
import com.jiubo.oa.service.ForgetCardReasonService;
import com.jiubo.oa.service.ForgetCardService;
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
 * @desc:忘记打卡服务实现类
 * @date: 2020-01-10 15:29
 * @author: dx
 * @version: 1.0
 */
@Service
public class ForgetCardServiceImpl extends ServiceImpl<ForgetCardDao, ForgetCardBean> implements ForgetCardService {

    //忘记打卡修改页面
    @Value("${modifyForgetCardUrl}")
    private String modifyForgetCardUrl;

    //忘记打卡审核页面
    @Value("${examineForgetCardUrl}")
    private String examineForgetCardUrl;

    @Autowired
    private ForgetCardDao forgetCardDao;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WxSendMessageService wxSendMessageService;

    @Autowired
    private ForgetCardReasonService forgetCardReasonService;

    @Override
    public List<ForgetCardBean> queryForgetCard(ForgetCardBean forgetCardBean) throws Exception {
        if (StringUtils.isNotBlank(forgetCardBean.getCreateEndDate()))
            forgetCardBean.setCreateEndDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.dateAdd(TimeUtil.parseAnyDate(forgetCardBean.getCreateEndDate()), TimeUtil.UNIT_DAY, 1)));
        if (StringUtils.isNotBlank(forgetCardBean.getForgetDateEnd()))
            forgetCardBean.setForgetDateEnd(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.dateAdd(TimeUtil.parseAnyDate(forgetCardBean.getForgetDateEnd()), TimeUtil.UNIT_DAY, 1)));
        return forgetCardDao.queryForgetCard(forgetCardBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addForgetCard(ForgetCardBean forgetCardBean) throws Exception {
        forgetCardBean.setState("0");
        forgetCardBean.setExaminerAdv("0");
        forgetCardBean.setAuditorAdv("0");
        forgetCardBean.setApproverAdv("0");
        forgetCardBean.setCreateDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime()));
        if (forgetCardDao.addForgetCard(forgetCardBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateForgetCard(ForgetCardBean forgetCardBean) throws Exception {
        if (forgetCardDao.updateForgetCard(forgetCardBean) <= 0) throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyForgetCard(ForgetCardBean forgetCardBean) throws Exception {
        if (StringUtils.isBlank(forgetCardBean.getEmpId())) throw new MessageException("申请人不能为空!");
        if (StringUtils.isBlank(forgetCardBean.getExaminer())) throw new MessageException("审查人不能为空!");
        if (StringUtils.isBlank(forgetCardBean.getAuditor())) throw new MessageException("审核人不能为空!");
        if (StringUtils.isBlank(forgetCardBean.getApprover())) throw new MessageException("批准人不能为空！");
        if (StringUtils.isBlank(forgetCardBean.getForgetDate())) throw new MessageException("忘记打卡日期不能为空!");
//        if (StringUtils.isBlank(forgetCardBean.getWitness())) throw new MessageException("证明人不能为空!");
        List<JSONObject> list = new ArrayList<JSONObject>();
        addForgetCard(forgetCardBean);
        applyForgetCardMsg(forgetCardBean, list, true);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }

    //申请忘记打卡，修改忘记打卡生成消息
    private void applyForgetCardMsg(ForgetCardBean forgetCardBean, List<JSONObject> list, boolean flag) throws Exception {
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(forgetCardBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        List<ForgetCardReasonBean> forgetCardReasonBeans = forgetCardReasonService.queryForgetCardReason(new ForgetCardReasonBean().setFcrId(forgetCardBean.getFcrId()));
        if (forgetCardReasonBeans.isEmpty()) throw new MessageException("未查询到未打卡原因!");
        ForgetCardReasonBean forgetCardReasonBean = forgetCardReasonBeans.get(0);
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
            content.put("value", flag ? "您已成功申请忘记打卡,请等待审核!" : "您已成功修改忘记打卡,请等待审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", forgetCardReasonBean.getFcrName());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //请假时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(forgetCardBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", flag ? "您已成功申请忘记打卡,请等待审核!" : "您已成功修改忘记打卡,请等待审核!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }

        //审查人消息
        employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(forgetCardBean.getExaminer()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        EmployeeBean examiner = employeeBeans.get(0);
        if (StringUtils.isNotBlank(examiner.getOpenId())) {
            //审查人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", examiner.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            jsonObject.put("url", examineForgetCardUrl.concat("?rdId=").concat(forgetCardBean.getFcId()));
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "有新的忘记打卡需要审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", forgetCardReasonBean.getFcrName());
            content.put("color", "#173177");
            data.put("keyword2", content);

            //申请时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(forgetCardBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", "有新的忘记打卡需要审核,点击查看详情!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operationForgetCard(ForgetCardBean forgetCardBean) throws Exception {

    }

    //忘记打卡取消、修改、审核
    private void operation(ForgetCardBean forgetCardBean, List<JSONObject> list) throws Exception {
        if (StringUtils.isBlank(forgetCardBean.getFcId())) throw new MessageException("id不能为空!");
        List<ForgetCardBean> forgetCardBeans = queryForgetCard(new ForgetCardBean().setFcId(forgetCardBean.getFcId()));
        if (forgetCardBeans.isEmpty()) throw new MessageException("未查询到该忘记打卡申请!");
        ForgetCardBean forgetCard = forgetCardBeans.get(0);
        if ("1".equals(forgetCard.getState())) throw new MessageException("该忘记打卡申请已被取消!");
        if (StringUtils.isNotBlank(forgetCardBean.getState())) {
            if ("1".equals(forgetCardBean.getState())) {
                //取消忘记打卡
                updateForgetCard(new ForgetCardBean().setFcId(forgetCard.getFcId()).setState("1"));
            } else {
                //修改忘记打卡,通知申请人+审查人
                forgetCardBean.setFcId(forgetCard.getFcId());
                forgetCardBean.setCreateDate(forgetCard.getCreateDate());
                updateForgetCard(forgetCardBean);
                applyForgetCardMsg(forgetCardBean,list,false);
            }
        } else if (StringUtils.isNotBlank(forgetCardBean.getExaminerAdv())) {
            //审查人审核
            if (!"0".equals(forgetCard.getExaminerAdv())) throw new MessageException("已完成审核,不可重复审核!");
            if (!forgetCard.getExaminer().equals(forgetCardBean.getExaminer())) throw new MessageException("不可代替他人审核!");

        } else if (StringUtils.isNotBlank(forgetCardBean.getAuditorAdv())) {
            //审核人审核
            if (!"0".equals(forgetCard.getAuditorAdv())) throw new MessageException("已完成审核,不可重复审核!");
            if (!forgetCard.getAuditor().equals(forgetCardBean.getAuditor())) throw new MessageException("不可代替他人审核!");

        } else if (StringUtils.isNotBlank(forgetCardBean.getApproverAdv())) {
            //批准人审核
            if (!"0".equals(forgetCard.getApproverAdv())) throw new MessageException("已完成审核,不可重复审核!");
            if (!forgetCard.getApprover().equals(forgetCardBean.getApprover())) throw new MessageException("不可代替他人审核!");

        }
    }

}
