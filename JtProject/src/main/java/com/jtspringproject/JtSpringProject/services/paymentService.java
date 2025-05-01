package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.dao.paymentDao;
import com.jtspringproject.JtSpringProject.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class paymentService {
    @Autowired
    private paymentDao paymentDao;

    public Payment savePayment(Payment payment) {
        return paymentDao.save(payment);
    }
}
