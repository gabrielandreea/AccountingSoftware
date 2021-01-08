package com.company.service;

import com.company.util.Constants;

import java.util.Scanner;

public class AuthServiceImpl implements AuthService{
    @Override
    public void authenticate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Username: ");
        //read user input
        String username = scanner.nextLine();

        System.out.println("Password: ");
        //read user input
        String password = scanner.nextLine();

        if (Constants.USERNAME.equals(username) && Constants.PASSWORD.equals(password)){
            System.out.println("You are authenticated!");
        }else {
            System.out.println("Incorrect credentials!");
            authenticate();
        }
    }
}
