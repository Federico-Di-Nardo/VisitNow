package com.example.visitnow;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class Database {
    private static Connection connection;

    public static Connection conClass(){
        String ip = "192.168.85.209", port = "1433", db = "VisitNow", username ="visitnow", password = "visitnow";
        //String ip = "192.168.1.240", port = "1433", db = "VisitNow", username ="visitnow", password = "visitnow";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String stringConexion;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            stringConexion = "jdbc:jtds:sqlserver://"+ip+":"+port+";databasename="+db+";user="+username+";password="+password+";";
            connection = DriverManager.getConnection(stringConexion);
        }catch(Exception e){
            Log.e("Error ", e.getMessage());
        }
        return connection;
    }

    public static ResultSet paisSP(String actividad, int usuario, int flgMigrar){
        try{
            Connection conexion = conClass();
            if (conexion != null){
                String query = "EXEC dbo.PaisSP (?,?,?)";
                CallableStatement stm = conexion.prepareCall(query);
                stm.setString(1,actividad);
                stm.setInt(2,usuario);
                stm.setInt(3,flgMigrar);
                System.out.println("EXEC dbo.PaisSP '"+actividad+"', '"+usuario+"', '"+flgMigrar+"'");
                stm.execute();
                return stm.getResultSet();
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static ResultSet usuarioSP(String actividad, int usuario, int idUsuario, String mail, Context context){
        try{
            Connection conexion = conClass();
            if (conexion != null){
                String query = "EXEC dbo.UsuarioSP (?,?,?,?)";
                CallableStatement stm = conexion.prepareCall(query);
                stm.setString(1,actividad);
                stm.setInt(2,usuario);
                stm.setInt(3,idUsuario);
                stm.setString(4,mail);
                stm.execute();
                ResultSet rs = stm.getResultSet();
                System.out.println("EXEC dbo.UsuarioSP '"+actividad+"', '"+usuario+"', '"+idUsuario+"', '"+mail+"'");
                SharedPreferences sharedPref = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putInt("idUsuario", rs.getInt(1));
                //editor.commit();

                return rs;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static ResultSet parametroSP(String actividad, int usuario, int parametro, int idUsuario, String codExtParametro, String parametroVal){
        try{
            Connection conexion = conClass();
            if (conexion != null){
                String query = "EXEC dbo.ParametroSP (?,?,?,?,?,?)";
                CallableStatement stm = conexion.prepareCall(query);
                stm.setString(1,actividad);
                stm.setInt(2,usuario);
                stm.setInt(3,parametro);
                stm.setInt(4,idUsuario);
                stm.setString(5,codExtParametro);
                stm.setString(6,parametroVal);
                System.out.println("EXEC dbo.ParametroSP '"+actividad+"', '"+usuario+"', '"+parametro+"', '"+idUsuario+"', '"+codExtParametro+"', '"+parametroVal+"'");
                stm.execute();
                ResultSet rs = stm.getResultSet();
                return rs;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static ResultSet nacionalidadSP(String actividad, int usuario, int idNacionalidad, int idUsuario, String paisNombre, int idPais){
        try{
            Connection conexion = conClass();
            if (conexion != null){
                String query = "EXEC dbo.NacionalidadSP (?,?,?,?,?,?)";
                CallableStatement stm = conexion.prepareCall(query);
                stm.setString(1,actividad);
                stm.setInt(2,usuario);
                stm.setInt(3,idNacionalidad);
                stm.setInt(4,idUsuario);
                stm.setString(5,paisNombre);
                stm.setInt(6,idPais);
                stm.execute();
                System.out.println("EXEC dbo.NacionalidadSP '"+actividad+"', '"+usuario+"', '"+idNacionalidad+"', '"+idUsuario+"', '"+paisNombre+"', '"+idPais+"' ");
                return stm.getResultSet();
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static ResultSet restriccionSP(String actividad, int usuario, int idRestriccion, String restriccionDes, String restriccionVal, String flgMigrar, String idPais, int idTipoParametro){
        try{
            Connection conexion = conClass();
            if (conexion != null){
                String query = "EXEC dbo.RestriccionSP (?,?,?,?,?,?,?,?)";
                CallableStatement stm = conexion.prepareCall(query);
                stm.setString(1,actividad);
                stm.setInt(2,usuario);
                stm.setInt(3,idRestriccion);
                stm.setString(4,restriccionDes);
                stm.setString(5,restriccionVal);
                stm.setString(6,flgMigrar);
                stm.setString(7,idPais);
                stm.setInt(8,idTipoParametro);
                stm.execute();
                System.out.println("EXEC dbo.RestriccionSP '"+actividad+"', '"+usuario+"', '"+idRestriccion+"', '"+restriccionDes+"', '"+restriccionVal+"', '"+flgMigrar+"', '"+idPais+"', '"+idTipoParametro+"' ");
                return stm.getResultSet();
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static ResultSet idiomaSP(String actividad, int usuario){
        try{
            Connection conexion = conClass();
            if (conexion != null){
                String query = "EXEC dbo.IdiomaSP (?,?)";
                CallableStatement stm = conexion.prepareCall(query);
                stm.setString(1,actividad);
                stm.setInt(2,usuario);
                stm.execute();
                return stm.getResultSet();
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static ResultSet nivelSP(String actividad, int usuario, String idiomasNivel){
        try{
            Connection conexion = conClass();
            if (conexion != null){
                String query = "EXEC dbo.NivelSP (?,?,?,?)";
                CallableStatement stm = conexion.prepareCall(query);
                stm.setString(1, actividad);
                stm.setInt(2, usuario);
                stm.setString(3, "");
                stm.setString(4, idiomasNivel);
                stm.execute();
                System.out.println("EXEC dbo.NivelSP '"+actividad+"', '"+usuario+"', '', '"+idiomasNivel+"'");
                return stm.getResultSet();
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static ResultSet usuarioIdiomaSP(String actividad, int usuario, int idUsuarioIdioma, int idUsuario, int idIdioma, int idNivel, String idiomaDes, String nivelDes){
        try{
            Connection conexion = conClass();
            if (conexion != null){
                String query = "EXEC dbo.IdiomaUsuarioSP (?,?,?,?,?,?,?,?)";
                CallableStatement stm = conexion.prepareCall(query);
                stm.setString(1,actividad);
                stm.setInt(2,usuario);
                stm.setInt(3,idUsuarioIdioma);
                stm.setInt(4,idUsuario);
                stm.setInt(5,idIdioma);
                stm.setInt(6,idNivel);
                stm.setString(7,idiomaDes);
                stm.setString(8,nivelDes);
                System.out.println("EXEC dbo.IdiomaUsuarioSP '"+actividad+"', '"+usuario+"', '"+idUsuarioIdioma+"', '"+idUsuario+"', '"+idIdioma+"', '"+idNivel+"', '"+idiomaDes+"', '"+nivelDes+"'");
                stm.execute();
                return stm.getResultSet();
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
}
