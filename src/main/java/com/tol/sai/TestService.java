package com.tol.sai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
 
    @Autowired
    private TestMapper mapper;
    
    public String selectNow() {
        return mapper.selectNow();
    }
}

