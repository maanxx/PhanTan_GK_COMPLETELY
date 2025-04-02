package iuh.fit.dao;

import iuh.fit.entity.Doctor;
import iuh.fit.util.AppUtils;
import org.neo4j.driver.Result;
import org.neo4j.driver.summary.ResultSummary;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DoctorDao implements DoctorDaoImpl{

    // add
    public boolean addDoctor(Doctor doctor) {

        String q = """
                CREATE (doc:Doctor{doctor_id:$doctor_id, 
                name:$name, phone:$phone, speciality:$speciality})
                RETURN doc;
                """;

        try (var session = AppUtils.getSession()) {
            return session.executeWrite(tx -> {
                ResultSummary summary = tx.run(q, AppUtils.toMap(doctor)).consume();

                return summary.counters().nodesCreated() > 0;

            });
        }
    }

    // thong ke
    public Map<String, Long> getNoOfDoctorsBySpeciality(String departmentName) {
        String q = """
                MATCH (doc:Doctor)-[:BELONG_TO]->(d:Department{name:$departmentName})
                RETURN doc.speciality AS chuyenKhoa, COUNT(doc) AS soLuong;
                """;
        try (var session = AppUtils.getSession()) {
            return session.executeRead(tx -> {
                Result result = tx.run(q, Map.of("departmentName", departmentName));
                return result.stream().collect(Collectors.toMap(
                        record -> record.get("chuyenKhoa").asString(),
                        record -> record.get("soLuong").asLong()
                ));
            });
        }
    }

    public List<Doctor> listDoctorsBySpeciality (String keywords) {
        String q = """
                    CALL db.index.fulltext.queryNodes("txt_index_speciality", $keywords) 
                    YIELD node, score RETURN node AS d;
                """;
        try (var session = AppUtils.getSession()) {
            return session.executeRead(tx -> {
                Result result = tx.run(q, Map.of("keywords", keywords));
                return result.stream()
                        .map(record -> record.get("d").asNode())
                        .map(node -> AppUtils.toDoctor(node))
                        .collect(Collectors.toList());
            });
        }
    }

    public boolean updateDiagnosis(String patientid, String doctorid, String newDiagnosis) {
        String query = """
                MATCH (p:Patient {patient_id: $patientId})-[r:BE_TREATED]->(d:Doctor {doctor_id: $doctorId})
                WHERE r.end_date IS NULL
                SET r.diagnosis = $newDiagnosis
                """;
        try (var session = AppUtils.getSession()) {
            return session.executeWrite(tx -> {
               ResultSummary summary = tx.run(query, Map.of(
                       "patient_id", patientid,
                       "doctor_id", doctorid,
                       "diagnosis", newDiagnosis
               )).consume();
               return summary.counters().nodesCreated() > 0;
            });
        }
    }
}
