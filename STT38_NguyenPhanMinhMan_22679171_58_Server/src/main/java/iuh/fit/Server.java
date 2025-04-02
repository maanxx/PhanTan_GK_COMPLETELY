package iuh.fit;

import iuh.fit.dao.DoctorDao;
import iuh.fit.dao.DoctorDaoImpl;
import iuh.fit.entity.Doctor;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(9171)){
            System.out.println("server running");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println(socket.getInetAddress().getHostName());
                System.out.println(socket.getPort());

                Thread thread = new Thread(new HandlingClient(socket));
                thread.start();
            }
        }
    }
}
class HandlingClient implements  Runnable{

    private Socket socket;
    private DoctorDaoImpl doctorDao;

    public HandlingClient(Socket socket) {
        this.socket = socket;
        doctorDao = new DoctorDao();
    }

    @Override
    public void run()  {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())){

            String request = in.readUTF();

            // add doctor
            if (request.equals("ADD_DOCTOR")) {

                String doctor_id = in.readUTF();
                String name = in.readUTF();
                String phone = in.readUTF();
                String speciality = in.readUTF();

                Doctor doctor = new Doctor(doctor_id, name, phone, speciality);
                boolean result = doctorDao.addDoctor(doctor);

                out.writeBoolean(result);
                out.flush();
            } else if (request.equals("THONGKE_DOCTOR")) {

                String departmentName = in.readUTF();

                Map<String, Long> doctor_SoLuong = doctorDao.getNoOfDoctorsBySpeciality(departmentName);

                out.writeObject(doctor_SoLuong);
                out.flush();
            } else if (request.equals("FIND_CHUYENKHOA")) {
                String keywords = in.readUTF();

                List<Doctor> doctorList = doctorDao.listDoctorsBySpeciality(keywords);

                out.writeObject(doctorList);
                out.flush();
            } else if (request.equals("EXIT")) {
                System.out.println("Client exited ..........!");

                socket.close();
            } else if (request.equals("updateDiagnosis")) {
                String patient_id = in.readUTF();
                String doctor_id = in.readUTF();
                String newDiagnosis = in.readUTF();

                boolean result = doctorDao.updateDiagnosis(patient_id, doctor_id, newDiagnosis);
                out.writeBoolean(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
