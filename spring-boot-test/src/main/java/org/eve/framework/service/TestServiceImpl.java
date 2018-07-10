package org.eve.framework.service;

import org.springframework.stereotype.Service;

/**
 * @author xiayc
 * @date 2018/7/9
 */
@Service
public class TestServiceImpl implements TestService {
    @Override
    public void print(String text) {
        System.out.println("=====================");
        System.out.println(text);
        System.out.println("=====================");
    }
}
