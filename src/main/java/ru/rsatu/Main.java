package ru.rsatu;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.runtime.QuarkusApplication;

@QuarkusMain
public class Main {

	public static void main(String ... args) {
        System.out.println("Running main method");
        Quarkus.run(args); 

	}

}
