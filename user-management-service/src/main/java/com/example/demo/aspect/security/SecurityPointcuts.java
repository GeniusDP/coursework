package com.example.demo.aspect.security;

import org.aspectj.lang.annotation.Pointcut;

public class SecurityPointcuts {

    @Pointcut("@annotation(com.example.demo.aspect.security.basic.SecuredRoute)")
    public void aroundSecuredMethod(){}

    @Pointcut("@annotation(com.example.demo.aspect.security.admin.AdminGrant)")
    public void aroundMethodWithAdminGrant(){}

}
