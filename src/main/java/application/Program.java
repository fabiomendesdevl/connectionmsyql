package application;

import db.DB;
import db.DbException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Connection connection = null;
        Statement st = null;
        ResultSet rs = null;

        PreparedStatement ps = null; //INSERIR DADOS

        int menu;
        do {
            System.out.println("Menu");
            System.out.println("1 - inserir dados de vendedor");
            System.out.println("2 - Atualizar dados");
            System.out.println("3 - Deletar dados");
            System.out.println("4 - Listar dados de departamento");
            System.out.println("5 - Listar dados de vendedor");
            System.out.print("Insira uma opção: ");
            menu = sc.nextInt();

            switch (menu) {
                case 1:
                    try {
                        System.out.print("Insira o nome: ");
                        sc.nextLine();
                        String nome = sc.nextLine();
                        System.out.print("Insira o email: ");
                        String email = sc.nextLine();

                        System.out.print("Insira o data de nascimento: (dd/MM/yyyy): ");
                        String dataStr = sc.nextLine();

                        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(sdf.parse(dataStr));
                        java.sql.Date data = java.sql.Date.valueOf(formattedDate);

                        System.out.print("Insira o salario base: ");
                        double salario = sc.nextDouble();
                        System.out.print("Insira o departamento 1,2,3 ou 4: ");
                        int departamento = sc.nextInt();

                        connection = DB.getConnection();
                        ps = connection.prepareStatement(
                                "INSERT INTO seller"
                                        + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
                                        + "VALUES"
                                        + "(?,?,?,?,?)");

                        ps.setString(1, nome);
                        ps.setString(2, email);
                        ps.setDate(3, data);
                        ps.setDouble(4, salario);
                        ps.setInt(5, departamento);

                        int rowsAffected = ps.executeUpdate();
                        System.out.println("Linhas afetadas: " + rowsAffected);

                    } catch (SQLException e) {
                        throw new DbException(e.getMessage());
                    } catch (ParseException e) {
                        throw new RuntimeException(e.getMessage());
                    } finally {
                        DB.closeStatement(st);
                        DB.closeConnection();
                    }
                    break;
                case 2:
                    try {
                        connection = DB.getConnection();
                        ps = connection.prepareStatement("UPDATE seller "
                                + "SET BaseSalary = BaseSalary + ? "
                                + "WHERE "
                                + "(DepartmentId = ?)");
                        ps.setDouble(1, 200);
                        ps.setDouble(2, 2);

                        int rowsAffected = ps.executeUpdate();

                        System.out.println("Dados atualizados: " + rowsAffected);

                    } catch (SQLException e) {
                        throw new DbException(e.getMessage());
                    }
                    break;
                case 3:
                    break;
                case 4:
                    try {
                        connection = DB.getConnection();
                        st = connection.createStatement();
                        rs = st.executeQuery("select * from department");
                        System.out.println("-------------------------------------------------");
                        while (rs.next()) {
                            System.out.println(rs.getInt("Id") + " | " + rs.getString("Name"));
                        }
                        System.out.println("-------------------------------------------------");
                    } catch (SQLException e) {
                        throw new DbException(e.getMessage()); //imprimir as mensagens de erros;
                    } finally {
                        DB.closeResultSet(rs);
                        DB.closeStatement(st);
                        DB.closeConnection();
                    }
                    break;
                case 5:
                    try {
                        connection = DB.getConnection();
                        st = connection.createStatement();
                        rs = st.executeQuery("select * from seller");
                        System.out.println("-------------------------------------------------");
                        while (rs.next()) {
                            System.out.println(rs.getInt("Id") + " | " + rs.getString("Name") + " | "
                                    + rs.getString("Email") + " | " + rs.getDate("BirthDate") + " | "
                                    + rs.getDouble("BaseSalary") + " | " + rs.getInt("DepartmentId"));
                        }
                        System.out.println("-------------------------------------------------");
                    } catch (SQLException e) {
                        throw new DbException(e.getMessage()); //imprimir as mensagens de erros;
                    } finally {
                        DB.closeResultSet(rs);
                        DB.closeStatement(st);
                        DB.closeConnection();
                    }
                    break;
                default:
                    break;
            }
        } while (menu != 0);
    }
}
