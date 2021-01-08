package com.company.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.company.util.Constants.*;


public class EmployeeRepositoryImpl implements EmployeeRepository {
    @Override
    public void insertLine(String csvLine) {
        //Adaugam o linie in plus continutului deja existent in employee.csv
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(CSV_FILE_NAME, true);
            fileWriter.append(NEW_LINES_SEPARATOR);
            fileWriter.append(csvLine);

            System.out.println("Employee was added succesfully!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while closing or flushing fileWriter !");
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> readCsv() {
        try {
            List<String> records = new ArrayList<>();
            try (BufferedReader bf = new BufferedReader(new FileReader(CSV_FILE_NAME))) {
                String line;
                boolean firstLineFlag = false;
                while ((line = bf.readLine()) != null) {
                    if (!firstLineFlag) {
                        firstLineFlag = true;
                        continue;
                    }
                    records.add(line);
                }
            }
            return records;
        } catch (IOException e) {
            System.out.println("Error while reading CSV !");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteLine(int employeeId) {
        //Pentru a putea sterge, citim tot fisierul employee.csv si in timp ce citim stocam doar ce vrem sa scriem la loc
        //sarim peste ceea ce am vrea sa stergem
        //Daca am lucra cu o baza de date nu ar fi nevoie sa facem acest artificiu.
        BufferedReader br = null;
        StringBuilder sb = null;
        try {
            br = new BufferedReader(new FileReader(CSV_FILE_NAME));
            sb = new StringBuilder();
            String line;
            boolean firstLineFlag = true;
            while ((line = br.readLine()) != null) {
                if (firstLineFlag) {
                    firstLineFlag = false;
                    sb.append(line);
                    sb.append(NEW_LINES_SEPARATOR);
                } else {
                    //verifica daca angajatul trebuie sa fie sters
                    String[] lineValues = line.split(COMMA_DELIMITER);
                    if (Integer.parseInt(lineValues[0]) != employeeId) {
                        sb.append(line);
                        sb.append(NEW_LINES_SEPARATOR);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error while deleting!");
            try {
                br.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        //Scriem informatia inapoi
        writeAllInformationToDatabase(sb);
    }


    @Override
    public void updateLine(int employeeId, String employeeDetails) {
        //Pentru a actualiza , citim linie cu linie, identificam linia cu eomployeeId, o inlocuim cu informatiile din
        //employeeDetails si apoi scriem toate liniile la loc pe disk
        BufferedReader br = null;
        StringBuilder sb = null;
        try {
            br = new BufferedReader(new FileReader(CSV_FILE_NAME));
            sb = new StringBuilder();
            String line;
            boolean fistLineFlag = true;
            while ((line = br.readLine()) != null) {
                if (fistLineFlag) {
                    fistLineFlag = false;
                    sb.append(line);
                    sb.append(NEW_LINES_SEPARATOR);
                } else {
                    String[] lineValues = line.split(COMMA_DELIMITER);
                    if (Integer.parseInt(lineValues[0]) == employeeId) {
                        sb.append(employeeDetails);
                        sb.append(NEW_LINES_SEPARATOR);
                    } else {
                        sb.append(line);
                        sb.append(NEW_LINES_SEPARATOR);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error while updating!");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeAllInformationToDatabase(sb);

    }

    private void writeAllInformationToDatabase(StringBuilder sb) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(new File(CSV_FILE_NAME));
            fw.write(sb.toString());
        } catch (Exception e) {
            System.out.println("There was an error while writting information after a delete!");
            e.printStackTrace();
        }finally {
            try {
                if (fw != null){
                    fw.flush();
                    fw.close();
                }
            }catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}