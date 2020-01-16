package com.jiubo.oa.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiubo.oa.bean.EmpEducationBean;
import com.jiubo.oa.bean.EmpFamilyBean;
import com.jiubo.oa.bean.EmpShiftBean;
import com.jiubo.oa.bean.EmployeeBean;
import com.jiubo.oa.common.Constant;
import com.jiubo.oa.dao.EmpEducationDao;
import com.jiubo.oa.dao.EmpFamilyDao;
import com.jiubo.oa.dao.EmpShiftDao;
import com.jiubo.oa.dao.EmployeeDao;
import com.jiubo.oa.exception.MessageException;
import com.jiubo.oa.service.EmployeeService;
import com.jiubo.oa.util.TimeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @date: 2020-01-02 15:49
 * @author: dx
 * @version: 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, EmployeeBean> implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private EmpEducationDao empEducationDao;

    @Autowired
    private EmpFamilyDao empFamilyDao;

    @Autowired
    private EmpShiftDao empShiftDao;


    @Override
    public List<EmployeeBean> queryEmployee(EmployeeBean employeeBean) {
        return employeeDao.queryEmployee(employeeBean);
    }


    @Override
    public List<EmployeeBean> queryDeptDrector(EmployeeBean employeeBean) {
        return employeeDao.queryDeptDrector(employeeBean);
    }

    @Override
    public JSONObject login(EmployeeBean employeeBean) throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isBlank(employeeBean.getAccName())) throw new MessageException("账号不能为空!");
        if (StringUtils.isBlank(employeeBean.getAccPwd())) throw new MessageException("密码不能为空!");
        List<EmployeeBean> employeeBeans = queryEmployee(new EmployeeBean().setAccName(employeeBean.getAccName()).setShowPwd("1"));
        if (employeeBeans.isEmpty()) throw new MessageException(employeeBean.getAccName() + "账号不存在!");
        EmployeeBean employee = employeeBeans.get(0);
        if (!employee.getAccPwd().equals(employeeBean.getAccPwd())) throw new MessageException("密码错误!");
        employee.setAccPwd("");
        jsonObject.put("empData", employee);
        return jsonObject;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployee(EmployeeBean employeeBean) throws Exception {
        if (employeeDao.updateEmployee(employeeBean) <= 0) throw new MessageException("操作失败!");
//        //获取学历信息
//        EmpEducationBean empEducationBean = employeeBean.getEmpEducationBean();
//        if (empEducationBean!=null) {
//            if (StringUtils.isBlank(empEducationBean.getEducatId())) throw  new MessageException("学历信息修改失败");
//            //根据主键修改员工学历信息
//            if (empEducationDao.updateByEducatId(empEducationBean)<=0) throw new MessageException("学历信息修改失败");
//        }
//        //获取员工家庭信息
//        List<EmpFamilyBean> empFamilyList = employeeBean.getEmpFamilyList();
//        if (!CollectionUtils.isEmpty(empFamilyList)) {
//            ArrayList<EmpFamilyBean> empFamilyBeans = new ArrayList<>();
//            for (EmpFamilyBean empFamilyBean : empFamilyList) {
//                if (StringUtils.isBlank(empFamilyBean.getFamiId())) {
//                    //如果没有主键，则为新增
//                    empFamilyBeans.add(empFamilyBean);
//                }
//            }
//            if (!CollectionUtils.isEmpty(empFamilyBeans)) {
//                //如果empFamilyBeans里面有数据，则新增
//                empFamilyDao.addEmpFamilyList(empFamilyBeans);
//            }
//
//            //批量更新员工家庭信息
//            if (empFamilyDao.updateByBeanList(empFamilyList)<=0) throw new MessageException("员工家庭信息修改失败");
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEmployee(EmployeeBean employeeBean) throws Exception {
        employeeBean.setState("1");
        if (employeeDao.addEmployee(employeeBean) <= 0) throw new MessageException("添加失败");
//        //获取学历信息
//        EmpEducationBean empEducationBean = employeeBean.getEmpEducationBean();
//        if (empEducationBean!=null) {
//            empEducationBean.setEmpId(employeeBean.getEmpId());
//            if (empEducationDao.addEmpEducation(empEducationBean)<=0) throw new MessageException("员工学历信息添加失败");
//        }
//        //获取员工家庭信息
//        List<EmpFamilyBean> empFamilyList = employeeBean.getEmpFamilyList();
//        if (!CollectionUtils.isEmpty(empFamilyList)) {
//            for (int i = 0; i < empFamilyList.size(); i++) {
//                empFamilyList.get(i).setEmpId(employeeBean.getEmpId());
//            }
//            if (empFamilyDao.addEmpFamilyList(empFamilyList)<=0) throw new MessageException("员工家庭信息添加失败");
//        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postTransfer(EmpShiftBean empShiftBean) throws Exception {
        //岗位调动
        if (employeeDao.postTransfer(empShiftBean) <= 0) throw new MessageException("岗位调动失败");
        //修改员工信息表的岗位信息
        EmployeeBean employeeBean = new EmployeeBean();
        employeeBean.setEmpId(empShiftBean.getEmpId());
        employeeBean.setDeptId(empShiftBean.getAfterDeptId());
        employeeBean.setPosId(empShiftBean.getAfterPosId());
        if (employeeDao.updateEmployee(employeeBean) <= 0) throw new MessageException("岗位调动失败");
    }

    @Override
    public EmployeeBean queryAllByEmpId(String empId) throws Exception {
        //查询员工详细信息
        EmployeeBean employeeBean = new EmployeeBean();
        employeeBean.setEmpId(empId);
        List<EmployeeBean> employeeBeanList = employeeDao.queryEmployee(employeeBean);
        if (CollectionUtils.isEmpty(employeeBeanList)) throw new MessageException("未查询到该人员信息，请稍后再试");
        EmployeeBean result = employeeBeanList.get(0);
        //查询员工家庭信息
        EmpFamilyBean empFamilyBean = new EmpFamilyBean();
        empFamilyBean.setEmpId(empId);
        List<EmpFamilyBean> empFamilyBeanList = empFamilyDao.queryEmpFamily(empFamilyBean);
        result.setEmpFamilyList(empFamilyBeanList);
        //查询员工学历信息
        EmpEducationBean empEducationBean = new EmpEducationBean();
        empEducationBean.setEmpId(empId);
        EmpEducationBean educationBean = empEducationDao.queryEmpEducation(empEducationBean);
        result.setEmpEducationBean(educationBean);
        //查询员工调岗信息
        EmpShiftBean empShiftBean = new EmpShiftBean();
        empShiftBean.setEmpId(empId);
        List<EmpShiftBean> empShiftBeanList = empShiftDao.queryEmpShift(empShiftBean);
        result.setEmpShiftBeanList(empShiftBeanList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindOpenId(EmployeeBean employeeBean) throws Exception {
        if (StringUtils.isBlank(employeeBean.getAccName())) throw new MessageException("账号不能为空!");
        if (StringUtils.isBlank(employeeBean.getAccPwd())) throw new MessageException("密码不能为空!");
        if (StringUtils.isBlank(employeeBean.getOpenId())) throw new MessageException("openId为空!");
        List<EmployeeBean> employeeBeans = queryEmployee(new EmployeeBean().setAccName(employeeBean.getAccName()).setShowPwd("1"));
        if (employeeBeans.isEmpty()) throw new MessageException(employeeBean.getAccName() + "账号不存在!");
        EmployeeBean employee = employeeBeans.get(0);
        if (!employee.getAccPwd().equals(employeeBean.getAccPwd())) throw new MessageException("密码错误!");
        if (StringUtils.isNotBlank(employee.getOpenId())) throw new MessageException("该账号已绑定微信，不可重复绑定!");
        employeeBeans = queryEmployee(new EmployeeBean().setOpenId(employeeBean.getOpenId()));
        if (employeeBeans.size() > 0) throw new MessageException("该微信已绑定账号,不可重复绑定!");
        updateEmployee(new EmployeeBean().setEmpId(employee.getEmpId()).setOpenId(employeeBean.getOpenId()));
    }

    @Override
    public List<EmployeeBean> queryAttendanceExamine(EmployeeBean employeeBean) throws Exception {
        //if (StringUtils.isBlank(employeeBean.getEmpId())) throw new MessageException("申请人id不能为空!");
        List<EmployeeBean> employeeBeans = queryEmployee(new EmployeeBean().setEmpId(employeeBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("申请人id错误!");
        EmployeeBean employee = employeeBeans.get(0);
        if ("院长".equals(employee.getPosName())) {
            return employeeBeans;
        } else if ("科长".equals(employee.getPosName()) || "主任".equals(employee.getPosName())) {
            if (StringUtils.isBlank(employeeBean.getState()) || "1".equals(employeeBean.getState())) {
                //返回人事科长
                return employeeDao.queryExamine(new EmployeeBean().setDeptName("人事科").setPosName("科长"));
            } else if ("2".equals(employeeBean.getState())) {
                //返回院长
                return employeeDao.queryExamine(new EmployeeBean().setDeptName("院办").setPosName("院长"));
            }
        } else {
            if (StringUtils.isBlank(employeeBean.getState()) || "1".equals(employeeBean.getState())) {
                //返回当前部门负责人
                return employeeDao.queryExamine(new EmployeeBean().setEmpId(employee.getLeaderId()));
            } else if ("2".equals(employeeBean.getState())) {
                //返回人事科长
                return employeeDao.queryExamine(new EmployeeBean().setDeptName("人事科").setPosName("科长"));
            } else if ("3".equals(employeeBean.getState())) {
                //返回院长
                return employeeDao.queryExamine(new EmployeeBean().setDeptName("院办").setPosName("院长"));
            }
        }
        return null;
    }

    @Override
    public List<EmployeeBean> queryReimbursementExamine(EmployeeBean employeeBean) throws Exception {
        List<EmployeeBean> employeeBeans = queryEmployee(new EmployeeBean().setEmpId(employeeBean.getEmpId()));
        if (employeeBeans.isEmpty()) throw new MessageException("申请人id错误!");
        EmployeeBean employee = employeeBeans.get(0);
        if ("院长".equals(employee.getPosName())) {
            return employeeBeans;
        } else if ("科长".equals(employee.getPosName()) || "主任".equals(employee.getPosName())) {
            if (StringUtils.isBlank(employeeBean.getState()) || "1".equals(employeeBean.getState())) {
                //返回财务科长
                return employeeDao.queryExamine(new EmployeeBean().setDeptName("财务科").setPosName("科长"));
            } else if ("2".equals(employeeBean.getState())) {
                //返回院长
                return employeeDao.queryExamine(new EmployeeBean().setDeptName("院办").setPosName("院长"));
            }
        } else {
            if (StringUtils.isBlank(employeeBean.getState()) || "1".equals(employeeBean.getState())) {
                //返回当前部门负责人
                return employeeDao.queryExamine(new EmployeeBean().setEmpId(employee.getLeaderId()));
            } else if ("2".equals(employeeBean.getState())) {
                //返回财务科长
                return employeeDao.queryExamine(new EmployeeBean().setDeptName("财务科").setPosName("科长"));
            } else if ("3".equals(employeeBean.getState())) {
                //返回院长
                return employeeDao.queryExamine(new EmployeeBean().setDeptName("院办").setPosName("院长"));
            }
        }
        return null;
    }
}
