package com.bupt.graduation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 代码长度计算
 *
 * @author wangz
 */
public class CodeLengthCountUtil {

    public static void main(String[] args) {
        count("C:\\Users\\wangz\\IdeaProjects\\graduation\\src\\main\\java\\com\\bupt\\graduation");
    }

    public static void count(String dir) {

        File file = new File(dir);

        ArrayList<File> list = getListFiles(file);

        System.out.println("========== 目录包含如下文件 : ==========");


        if (list == null) {
            return;
        }
        for (File f : list) {
            System.out.println(f.getName());
        }
        System.out.println("=========代码的总长度为=========");


        int sum = 0;

        for (File f : list) {
            sum += readCountLine(f);
        }
        System.out.println(sum + " 行");
    }

    private static ArrayList<File> getListFiles(File file) {

        ArrayList<File> files = new ArrayList<>();

        if ((file).isFile()) {

            files.add(file);

            return files;

        } else if ((file).isDirectory()) {

            File[] fileArr = (file).listFiles();

            if (fileArr == null) {
                return null;
            }
            for (File fileOne : fileArr) {

                files.addAll(getListFiles(fileOne));
            }
        }
        return files;
    }

    private static int readCountLine(File file) {
        int count = 0;

        try {
            Scanner sc = new Scanner(new FileInputStream(file));
            while (sc.hasNextLine()) {
                count++;
                sc.nextLine();
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return count;
    }
}
