package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.bean.ReimbursementAccountBean;
import com.jiubo.oa.bean.ReimbursementCertificateBean;
import com.jiubo.oa.dao.ReimbursementAccountDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmployeeService;
import com.jiubo.oa.service.ReimbursementAccountService;
import com.jiubo.oa.service.ReimbursementCertificateService;
import com.jiubo.oa.service.WxSendMessageService;
import com.jiubo.oa.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @date: 2020-01-15 10:26
 * @author: dx
 * @version: 1.0
 */
@Service
public class ReimbursementAccountServiceImpl extends ServiceImpl<ReimbursementAccountDao, ReimbursementAccountBean> implements ReimbursementAccountService {

    @Value("${oADir}")
    private String oADir;

    @Autowired
    private EmployeeService employeeService;

    //报销单审核页面
    @Value("${examineReimburAccountUrl}")
    private String examineReimburAccountUrl;

    //报销单修改页面
    @Value("${modifyReimburAccountUrl}")
    private String modifyReimburAccountUrl;

    @Autowired
    private WxSendMessageService wxSendMessageService;

    @Autowired
    private ReimbursementAccountDao reimbursementAccountDao;

    @Autowired
    private ReimbursementCertificateService certificateService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean) throws Exception {
        reimbursementAccountBean.setState("0");
        reimbursementAccountBean.setExaminerAdv("0");
        reimbursementAccountBean.setAuditorAdv("0");
        reimbursementAccountBean.setApproverAdv("0");
        reimbursementAccountBean.setCreateDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime()));
        if (reimbursementAccountDao.addReimbursementAccount(reimbursementAccountBean) <= 0)
            throw new MessageException("操作失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean) throws Exception {
        if (reimbursementAccountDao.updateReimbursementAccount(reimbursementAccountBean) <= 0)
            throw new MessageException("操作失败!");
    }

    @Override
    public List<ReimbursementAccountBean> queryReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean) throws Exception {
        if (StringUtils.isNotBlank(reimbursementAccountBean.getCreateEndDate()))
            reimbursementAccountBean.setCreateEndDate(TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.dateAdd(TimeUtil.parseAnyDate(reimbursementAccountBean.getCreateEndDate()), TimeUtil.UNIT_DAY, 1)));
        return reimbursementAccountDao.queryReimbursementAccount(reimbursementAccountBean);
    }

    @Override
    public void applyReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean, MultipartFile[] file) throws Exception {
        if (StringUtils.isBlank(reimbursementAccountBean.getEmpId())) throw new MessageException("申请人不能为空!");
        if (StringUtils.isBlank(reimbursementAccountBean.getExaminer())) throw new MessageException("审查人不能为空!");
        if (StringUtils.isBlank(reimbursementAccountBean.getAuditor())) throw new MessageException("审核人不能为空!");
        //if (StringUtils.isBlank(forgetCardBean.getApprover())) throw new MessageException("批准人不能为空！");
        List<JSONObject> list = new ArrayList<JSONObject>();
        addReimbursementAccount(reimbursementAccountBean);
        if (file != null) {
            for (MultipartFile multipartFile : file) {
                ReimbursementCertificateBean reimbursementCertificateBean = new ReimbursementCertificateBean();
                reimbursementCertificateBean.setRaId(reimbursementAccountBean.getRaId());
                certificateService.addReimbursementCertificate(reimbursementCertificateBean);
                //生成文件名（凭证id + 报销id ）
                String fileName = multipartFile.getOriginalFilename();
                fileName = fileName.substring(fileName.lastIndexOf("."));

                String path = oADir.endsWith("/") ? oADir.concat("certificateDir/") : oADir.concat("/".concat("certificateDir/"));
                File dir = new File(path);
                if (!dir.exists()) dir.mkdirs();
                path = path.concat(reimbursementCertificateBean.getRcId()).concat("_").concat(reimbursementAccountBean.getRaId()).concat(fileName);
                reimbursementCertificateBean.setImg(path);
                certificateService.updateReimbursementCertificate(reimbursementCertificateBean);

                //读写文件
                if (!multipartFile.isEmpty()) {
                    InputStream is = multipartFile.getInputStream();
                    int len = 0;
                    byte[] by = new byte[1024];
                    OutputStream os = new FileOutputStream(path);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    BufferedOutputStream bos = new BufferedOutputStream(os);
                    while ((len = bis.read(by)) != -1) {
                        bos.write(by, 0, len);
                        bos.flush();
                    }
                    if (bos != null)
                        bos.close();
                    if (bis != null)
                        bis.close();
                    if (os != null)
                        os.close();
                    if (is != null)
                        is.close();
                }
            }
        }
        applyReimbursementAccountMsg(reimbursementAccountBean, list, true);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }

    //申请人消息
    private void applyReimbursementAccountMsg(ReimbursementAccountBean
                                                      reimbursementAccountBean, List<JSONObject> list, boolean flag) throws Exception {
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(reimbursementAccountBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
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
            content.put("value", flag ? "您已成功申请报销单,请等待审核!" : "您已成功修改报销单,请等待审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", "报销单");
            content.put("color", "#173177");
            data.put("keyword2", content);

            //请假时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(reimbursementAccountBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", flag ? "您已成功报销单,请等待审核!" : "您已成功修改报销单,请等待审核!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }

        //审查人消息
        employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(reimbursementAccountBean.getExaminer()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        EmployeeBean examiner = employeeBeans.get(0);
        if (StringUtils.isNotBlank(examiner.getOpenId())) {
            //审查人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", examiner.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            jsonObject.put("url", examineReimburAccountUrl.concat("?raId=").concat(reimbursementAccountBean.getRaId()));
            data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "有新的报销单需要审核!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", "报销单");
            content.put("color", "#173177");
            data.put("keyword2", content);

            //申请时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(reimbursementAccountBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", "有新的报销单需要审核,点击查看详情!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
    }

    @Override
    public void operationReimbursementAccount(ReimbursementAccountBean reimbursementAccountBean, MultipartFile[] file) throws Exception {
        List<JSONObject> list = new ArrayList<JSONObject>();
        operation(reimbursementAccountBean, file, list);
        for (JSONObject jsonObject : list) {
            wxSendMessageService.sendMessage(jsonObject);
        }
    }

    //忘记打卡取消、修改、审核
    private void operation(ReimbursementAccountBean reimbursementAccountBean, MultipartFile[]
            file, List<JSONObject> list) throws Exception {
        if (StringUtils.isBlank(reimbursementAccountBean.getRaId())) throw new MessageException("id不能为空!");
        List<ReimbursementAccountBean> reimbursementAccountBeans = queryReimbursementAccount(new ReimbursementAccountBean().setRaId(reimbursementAccountBean.getRaId()));
        if (reimbursementAccountBeans.isEmpty()) throw new MessageException("未查询到该报销单!");
        ReimbursementAccountBean reimbursementAccount = reimbursementAccountBeans.get(0);
        List<EmployeeBean> employeeBeans = null;
        JSONObject jsonObject = null;
        JSONObject data = null;
        if ("1".equals(reimbursementAccount.getState())) throw new MessageException("该报销单已被取消!");
        if (StringUtils.isNotBlank(reimbursementAccountBean.getState())) {
            if ("1".equals(reimbursementAccountBean.getState())) {
                //取消报销单
                updateReimbursementAccount(new ReimbursementAccountBean().setRaId(reimbursementAccount.getRaId()).setState("1"));
            } else {
                //修改报销单,通知申请人+审查人
                reimbursementAccountBean.setState("0");
                reimbursementAccountBean.setExaminerAdv("0");
                reimbursementAccountBean.setAuditorAdv("0");
                reimbursementAccountBean.setApproverAdv("0");
                reimbursementAccountBean.setRaId(reimbursementAccount.getRaId());
                reimbursementAccountBean.setCreateDate(reimbursementAccount.getCreateDate());
                updateReimbursementAccount(reimbursementAccountBean);
                if (file != null) {

                }
                applyReimbursementAccountMsg(reimbursementAccountBean, list, false);
            }
        } else if (StringUtils.isNotBlank(reimbursementAccountBean.getExaminerAdv())) {
            //审查人审核
            if (!"0".equals(reimbursementAccount.getExaminerAdv())) throw new MessageException("已完成审核,不可重复审核!");
            if (!reimbursementAccount.getExaminer().equals(reimbursementAccountBean.getExaminer()))
                throw new MessageException("不可代替他人审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(reimbursementAccountBean.getExaminerAdv())) {
                //不同意，发消息给申请人
                updateReimbursementAccount(new ReimbursementAccountBean().setRaId(reimbursementAccount.getRaId()).setExaminerAdv("2").setExaminerRemark(reimbursementAccountBean.getExaminerRemark()).setExaminerDate(nowStr));
                operationReimbursementEmpMsg(reimbursementAccount, list);
            } else {
                //同意，通知审核人审核
                updateReimbursementAccount(new ReimbursementAccountBean().setRaId(reimbursementAccount.getRaId()).setExaminerAdv("1").setExaminerRemark(reimbursementAccountBean.getExaminerRemark()).setExaminerDate(nowStr));
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(reimbursementAccount.getAuditor()));
                if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                EmployeeBean auditor = employeeBeans.get(0);
                if (StringUtils.isNotBlank(auditor.getOpenId())) {
                    //审核人消息
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", auditor.getOpenId());
                    jsonObject.put("url", examineReimburAccountUrl.concat("?raId=").concat(reimbursementAccount.getRaId()));
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "有新的报销单需要审核!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", reimbursementAccount.getEmpName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", "报销单");
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //请假时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(reimbursementAccount.getCreateDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "有新的报销单需要审核,点击查看详情!");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            }
        } else if (StringUtils.isNotBlank(reimbursementAccountBean.getAuditorAdv())) {
            //审核人审核
            if (!"0".equals(reimbursementAccount.getAuditorAdv())) throw new MessageException("已完成审核,不可重复审核!");
            if (!reimbursementAccount.getAuditor().equals(reimbursementAccountBean.getAuditor()))
                throw new MessageException("不可代替他人审核!");
            if (!"1".equals(reimbursementAccount.getExaminerAdv())) throw new MessageException("审查人未同意前不可审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(reimbursementAccountBean.getAuditorAdv())) {
                //不同意，发消息给申请人
                updateReimbursementAccount(new ReimbursementAccountBean().setRaId(reimbursementAccount.getRaId()).setAuditorAdv("2").setAuditorRemark(reimbursementAccountBean.getAuditorRemark()).setAuditorDate(nowStr));
                operationReimbursementEmpMsg(reimbursementAccount, list);
            } else {
                //同意，通知批准人审核
                updateReimbursementAccount(new ReimbursementAccountBean().setRaId(reimbursementAccount.getRaId()).setAuditorAdv("1").setAuditorRemark(reimbursementAccountBean.getAuditorRemark()).setAuditorDate(nowStr));
                if (StringUtils.isBlank(reimbursementAccount.getApprover())) {
                    employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(reimbursementAccount.getEmpId()));
                    if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
                    EmployeeBean employeeBean = employeeBeans.get(0);
                    if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                        jsonObject = new JSONObject();
                        jsonObject.put("touser", employeeBean.getOpenId());
                        jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                        data = new JSONObject();

                        JSONObject content = new JSONObject();
                        content.put("value", "您申请的报销单已通过审核!");
                        content.put("color", "#173177");
                        data.put("first", content);

                        //申请人
                        content = new JSONObject();
                        content.put("value", employeeBean.getEmpName());
                        content.put("color", "#173177");
                        data.put("keyword1", content);

                        //请假类型
                        content = new JSONObject();
                        content.put("value", "忘记打卡");
                        content.put("color", "#173177");
                        data.put("keyword2", content);

                        //请假时间
                        content = new JSONObject();
                        content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(reimbursementAccount.getCreateDate())));
                        content.put("color", "#173177");
                        data.put("keyword3", content);

                        //备注
                        content = new JSONObject();
                        content.put("value", "您申请的报销单已通过!");
                        content.put("color", "#173177");
                        data.put("remark", content);

                        jsonObject.put("data", data);

                        list.add(jsonObject);
                    }
                } else {
                    employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(reimbursementAccount.getApprover()));
                    if (employeeBeans.isEmpty()) throw new MessageException("审核人工号错误!");
                    EmployeeBean auditor = employeeBeans.get(0);
                    if (StringUtils.isNotBlank(auditor.getOpenId())) {
                        //审核人消息
                        jsonObject = new JSONObject();
                        jsonObject.put("touser", auditor.getOpenId());
                        jsonObject.put("url", examineReimburAccountUrl.concat("?raId=").concat(reimbursementAccount.getRaId()));
                        jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                        data = new JSONObject();

                        JSONObject content = new JSONObject();
                        content.put("value", "有新的报销单需要审核!");
                        content.put("color", "#173177");
                        data.put("first", content);

                        //申请人
                        content = new JSONObject();
                        content.put("value", reimbursementAccount.getEmpName());
                        content.put("color", "#173177");
                        data.put("keyword1", content);

                        //请假类型
                        content = new JSONObject();
                        content.put("value", "报销单");
                        content.put("color", "#173177");
                        data.put("keyword2", content);

                        //请假时间
                        content = new JSONObject();
                        content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(reimbursementAccount.getCreateDate())));
                        content.put("color", "#173177");
                        data.put("keyword3", content);

                        //备注
                        content = new JSONObject();
                        content.put("value", "有新的报销单需要审核,点击查看详情!");
                        content.put("color", "#173177");
                        data.put("remark", content);

                        jsonObject.put("data", data);

                        list.add(jsonObject);
                    }
                }
            }
        } else if (StringUtils.isNotBlank(reimbursementAccountBean.getApproverAdv())) {
            //批准人审核
            if (!"0".equals(reimbursementAccount.getApproverAdv())) throw new MessageException("已完成审核,不可重复审核!");
            if (StringUtils.isBlank(reimbursementAccount.getApprover()))
                throw new MessageException("该申请只需2个审核人审核，不可审核!");
            if (!reimbursementAccount.getApprover().equals(reimbursementAccountBean.getApprover()))
                throw new MessageException("不可代替他人审核!");
            if (!"1".equals(reimbursementAccount.getAuditorAdv())) throw new MessageException("审核人未同意前不可审核!");
            String nowStr = TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.getDBTime());
            if ("2".equals(reimbursementAccountBean.getApproverAdv())) {
                //不同意，发消息给申请人
                updateReimbursementAccount(new ReimbursementAccountBean().setRaId(reimbursementAccount.getRaId()).setApproverAdv("2").setApproverRemark(reimbursementAccountBean.getApproverRemark()).setApproverDate(nowStr));
                operationReimbursementEmpMsg(reimbursementAccount, list);
            } else {
                //同意，发消息给申请人
                updateReimbursementAccount(new ReimbursementAccountBean().setRaId(reimbursementAccount.getRaId()).setApproverAdv("1").setApproverRemark(reimbursementAccountBean.getApproverRemark()).setApproverDate(nowStr));
                employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(reimbursementAccount.getEmpId()));
                if (employeeBeans.isEmpty()) throw new MessageException("员工工号错误!");
                EmployeeBean employeeBean = employeeBeans.get(0);
                if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
                    jsonObject = new JSONObject();
                    jsonObject.put("touser", employeeBean.getOpenId());
                    jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
                    data = new JSONObject();

                    JSONObject content = new JSONObject();
                    content.put("value", "您申请的报销单已通过审核!");
                    content.put("color", "#173177");
                    data.put("first", content);

                    //申请人
                    content = new JSONObject();
                    content.put("value", employeeBean.getEmpName());
                    content.put("color", "#173177");
                    data.put("keyword1", content);

                    //请假类型
                    content = new JSONObject();
                    content.put("value", "报销单");
                    content.put("color", "#173177");
                    data.put("keyword2", content);

                    //请假时间
                    content = new JSONObject();
                    content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(reimbursementAccount.getCreateDate())));
                    content.put("color", "#173177");
                    data.put("keyword3", content);

                    //备注
                    content = new JSONObject();
                    content.put("value", "您申请的报销单已通过!");
                    content.put("color", "#173177");
                    data.put("remark", content);

                    jsonObject.put("data", data);

                    list.add(jsonObject);
                }
            }
        }

    }


    //不同意申请人消息
    private void operationReimbursementEmpMsg(ReimbursementAccountBean
                                                      reimbursementAccountBean, List<JSONObject> list) throws Exception {
        List<EmployeeBean> employeeBeans = employeeService.queryEmployee(new EmployeeBean().setEmpId(reimbursementAccountBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("未查询到该员工!");
        EmployeeBean employeeBean = employeeBeans.get(0);
        JSONObject jsonObject = null;
        if (StringUtils.isNotBlank(employeeBean.getOpenId())) {
            //申请人消息
            jsonObject = new JSONObject();
            jsonObject.put("touser", employeeBean.getOpenId());
            jsonObject.put("template_id", "-ji2ofkXT1lxWlWwcvUvcXUBeOsGVG9rGrbfmPC36lU");
            jsonObject.put("url", modifyReimburAccountUrl.concat("?raId=").concat(reimbursementAccountBean.getRaId()));
            JSONObject data = new JSONObject();

            JSONObject content = new JSONObject();
            content.put("value", "报销单审核未通过!");
            content.put("color", "#173177");
            data.put("first", content);

            //申请人
            content = new JSONObject();
            content.put("value", employeeBean.getEmpName());
            content.put("color", "#173177");
            data.put("keyword1", content);

            //请假类型
            content = new JSONObject();
            content.put("value", "报销单");
            content.put("color", "#173177");
            data.put("keyword2", content);

            //请假时间
            content = new JSONObject();
            content.put("value", TimeUtil.getDateYYYY_MM_DD_HH_MM_SS(TimeUtil.parseAnyDate(reimbursementAccountBean.getCreateDate())));
            content.put("color", "#173177");
            data.put("keyword3", content);

            //备注
            content = new JSONObject();
            content.put("value", "您申请的报销单未通过审核，点击查看详情!");
            content.put("color", "#173177");
            data.put("remark", content);

            jsonObject.put("data", data);

            list.add(jsonObject);
        }
    }
}
