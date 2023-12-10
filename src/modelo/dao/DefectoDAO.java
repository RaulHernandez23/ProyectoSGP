package modelo.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import modelo.ConectorBaseDatos;
import modelo.pojo.Defecto;

public class DefectoDAO {

    public static HashMap<String, Object> registrarDefecto(Defecto defecto) {

        HashMap<String, Object> respuesta = new LinkedHashMap<String, Object>();
        respuesta.put("error", true);
        Connection conexionBD = ConectorBaseDatos.obtenerConexion();

        if (conexionBD != null) {

            try {

                String consulta = "INSERT INTO defecto "
                        + "(titulo, "
                        + "descripcion, "
                        + "fechaReporte, "
                        + "idEstadoDefecto, "
                        + "idEstudiante, "
                        + "idProyecto) "
                        + "VALUES (?, ?, NOW(), 2, ?, ?);";

                PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                sentencia.setString(1, defecto.getTitulo());
                sentencia.setString(2, defecto.getDescripcion());
                sentencia.setInt(3, defecto.getIdEstudiante());
                sentencia.setInt(4, defecto.getIdProyecto());
                int filasAfectadas = sentencia.executeUpdate();
                conexionBD.close();
                
                if (filasAfectadas > 0) {

                    respuesta.put("error", false);
                    respuesta.put("mensaje", "Defecto registrado exitosamente");

                } else {
                    respuesta.put("mensaje", "No se pudo registrar el defecto");
                }

            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
                respuesta.put("mensaje", "Error al registrar el defecto");
            }
        } else {
            respuesta.put("mensaje", "No se pudo conectar a la base de datos, intentélo más tarde");
        }

        return respuesta;
    }

    public static HashMap<String, Object> consultarDefectos() {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
    
        Connection conexion = ConectorBaseDatos.obtenerConexion();
    
        if (conexion != null) {
            try {
                String consulta = "SELECT idDefecto, titulo, d.descripcion, " +
                        "esfuerzoMinutos, fechaReporte, fechaFin, d.idEstadoDefecto, " +
                        "d.idEstudiante, ed.estado AS estadoActividad, " +
                        "CONCAT(e.nombre, ' ', e.apellidoPaterno, ' ', " +
                        "e.apellidoMaterno) AS estudiante FROM defecto d INNER JOIN " +
                        "estadodefecto ed ON ed.idEstadoDefecto = d.idEstadoDefecto " +
                        "INNER JOIN estudiante e ON d.idEstudiante = e.idEstudiante " + 
                        "INNER JOIN proyecto p on p.idProyecto = d.idProyecto " +
                        "WHERE p.idProyecto = 1 " +
                        "ORDER BY fechaReporte ASC, estudiante ASC";
    
                PreparedStatement sentencia = conexion.prepareStatement(consulta);
    
                ResultSet resultado = sentencia.executeQuery();
    
                ArrayList<HashMap<String, Object>> listaDefectos = new ArrayList<>();
    
                while (resultado.next()) {
                    HashMap<String, Object> defectoMap = new HashMap<>();
                    defectoMap.put("idDefecto", resultado.getInt("idDefecto"));
                    defectoMap.put("titulo", resultado.getString("titulo"));
                    defectoMap.put("descripcion", resultado.getString("descripcion"));
                    defectoMap.put("esfuerzoMinutos", resultado.getInt("esfuerzoMinutos"));
                    defectoMap.put("fechaReporte", resultado.getString("fechaReporte"));
                    defectoMap.put("fechaFin", resultado.getString("fechaFin"));
                    defectoMap.put("idEstadoDefecto", resultado.getInt("idEstadoDefecto"));
                    defectoMap.put("idEstudiante", resultado.getInt("idEstudiante"));
                    defectoMap.put("estadoActividad", resultado.getString("estadoActividad"));
                    defectoMap.put("estudiante", resultado.getString("estudiante"));
    
                    listaDefectos.add(defectoMap);
                }
    
                respuesta.put("error", false);
                respuesta.put("defectos", listaDefectos);
    
            } catch (SQLException se) {
                se.printStackTrace();
                respuesta.put("mensaje", "Error en la base de datos: " + se.getMessage());
            } finally {
                ConectorBaseDatos.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No se pudo conectar a la base de datos, inténtelo más tarde");
        }
    
        return respuesta;
    }
    
    public static HashMap<String, Object> consultarDefectosProyecto(Integer idProyecto) {

        HashMap<String, Object> respuesta = new HashMap<String, Object>();

        respuesta.put("error", true);

        Connection conexionBD = ConectorBaseDatos.obtenerConexion();

        if (conexionBD != null) {

            try {

                String consulta = "SELECT d.idDefecto, " +
                        "d.titulo, d.descripcion, " +
                        "d.esfuerzoMinutos, d.fechaReporte, " +
                        "d.idEstadoDefecto, " +
                        "ed.estado AS estadoDefecto, " +
                        "d.idEstudiante, " +
                        "CONCAT(e.nombre, ' ', e.apellidoPaterno, " +
                        "' ', e.apellidoMaterno) AS nombreEstudiante " +
                        "FROM defecto d INNER JOIN estudiante e " +
                        "ON d.idEstudiante = e.idEstudiante " +
                        "INNER JOIN estadodefecto ed " +
                        "ON d.idEstadoDefecto = ed.idEstadoDefecto " +
                        "WHERE d.idProyecto = ?";

                PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                sentencia.setInt(1, idProyecto);

                ResultSet resultadoConsulta = sentencia.executeQuery();

                ArrayList<Defecto> defectos = new ArrayList<Defecto>();

                while (resultadoConsulta.next()) {

                    Defecto defecto = new Defecto();
                    defecto.setIdDefecto(resultadoConsulta.getInt("idDefecto"));
                    defecto.setTitulo(resultadoConsulta.getString("titulo"));
                    defecto.setDescripcion(resultadoConsulta.getString("descripcion"));
                    defecto.setEsfuerzoMinutos(resultadoConsulta.getInt("esfuerzoMinutos"));
                    defecto.setFechaReporte(resultadoConsulta.getString("fechaReporte"));
                    defecto.setIdEstadoDefecto(resultadoConsulta.getInt("idEstadoDefecto"));
                    defecto.setIdEstudiante(resultadoConsulta.getInt("idEstudiante"));
                    defecto.setEstadoDefecto(resultadoConsulta.getString("estadoDefecto"));
                    defecto.setNombreEstudiante(resultadoConsulta.getString("nombreEstudiante"));

                    defectos.add(defecto);

                }

                respuesta.put("error", false);
                respuesta.put("defectos", defectos);

            } catch (SQLException se) {

                respuesta.put("mensaje", "Error" + se.getMessage());

            } finally {
                ConectorBaseDatos.cerrarConexion(conexionBD);
            }

        } else {
            respuesta.put("mensaje", "Error en la conexión con la base de datos");
        }

        return respuesta;

    }
    
    public static HashMap<String, Object> consultarNombresDefectosProyecto(Integer idProyecto) {

        HashMap<String, Object> respuesta = new HashMap<String, Object>();

        respuesta.put("error", true);

        Connection conexionBD = ConectorBaseDatos.obtenerConexion();

        if (conexionBD != null) {

            try {

                String consulta = "SELECT idDefecto, titulo " +
                        "FROM defecto " +
                        "WHERE idProyecto = ?";

                PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
                sentencia.setInt(1, idProyecto);

                ResultSet resultadoConsulta = sentencia.executeQuery();

                ArrayList<Defecto> defectos = new ArrayList<Defecto>();

                while (resultadoConsulta.next()) {

                    Defecto defecto = new Defecto();
                    defecto.setIdDefecto(resultadoConsulta.getInt("idDefecto"));
                    defecto.setTitulo(resultadoConsulta.getString("titulo"));

                    defectos.add(defecto);

                }

                respuesta.put("error", false);
                respuesta.put("defectos", defectos);

            } catch (SQLException se) {

                respuesta.put("mensaje", "Error" + se.getMessage());

            } finally {
                ConectorBaseDatos.cerrarConexion(conexionBD);
            }

        } else {
            respuesta.put("mensaje", "Error en la conexión con la base de datos");
        }

        return respuesta;
    }

}
