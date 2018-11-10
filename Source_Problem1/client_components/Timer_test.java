package client_components;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.*;
import java.util.Scanner;

public class Timer_test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long j = System.currentTimeMillis();
		System.out.println(j);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String t = in.readLine();
		System.out.println(t);
		
		long k = System.currentTimeMillis() - j;
		System.out.println(k);

	}

}
