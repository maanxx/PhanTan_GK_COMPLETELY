package iuh.fit;

import iuh.fit.entity.Doctor;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("172.16.1.86", 9171);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner sc = new Scanner(System.in)
        )
        {
            while (true) {
                System.out.println("Nhap cau so thu: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1 -> {
                        // add doctor
                        out.writeUTF("ADD_DOCTOR");

                        out.writeUTF("DR.202");
                        out.writeUTF("Minhmmm");
                        out.writeUTF("0123456722");
                        out.writeUTF("AAA");

                        boolean result = in.readBoolean();
                        if (result) {
                            System.out.println("Success");
                        } else {
                            System.out.println("Fail");
                        }

                    }
                    case 2 -> {
                        out.writeUTF("THONGKE_DOCTOR");

                        out.writeUTF("Dermatology");

                        Map<String, Long> doctor_SoLuong = (Map<String, Long>) in.readObject();

                        doctor_SoLuong.entrySet().forEach(entry -> {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                        });
                    }
                    case 3 -> {
                        out.writeUTF("FIND_CHUYENKHOA");

                        out.writeUTF("Obstetrics");

                        List<Doctor> doctorList = (List<Doctor>) in.readObject();

                        doctorList.forEach(System.out::println);
                    }
                    case 4 -> {
                        out.writeUTF("updateDiagnosis");

                        out.writeUTF("PT003");
                        out.writeUTF("DR.001");
                        out.writeUTF("aaa");

                        boolean result = in.readBoolean();
                        if (result) {
                            System.out.println("Success");
                        } else {
                            System.out.println("Fail");
                        }
                    }
                    case 5 -> {

                        System.out.println("Exitting ..........!");
                        out.writeUTF("EXIT");
                        return;
                    }

                }
            }


        }
    }
}
