/*
* Copyright (C) 2007-2007 the GSAN - Sistema Integrado de Gestão de Serviços de Saneamento
*
* This file is part of GSAN, an integrated service management system for Sanitation
*
* GSAN is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License.
*
* GSAN is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA
*/

/*
* GSAN - Sistema Integrado de Gestão de Serviços de Saneamento
* Copyright (C) <2007> 
* Adriano Britto Siqueira
* Alexandre Santos Cabral
* Ana Carolina Alves Breda
* Ana Maria Andrade Cavalcante
* Aryed Lins de Araújo
* Bruno Leonardo Rodrigues Barros
* Carlos Elmano Rodrigues Ferreira
* Cláudio de Andrade Lira
* Denys Guimarães Guenes Tavares
* Eduardo Breckenfeld da Rosa Borges
* Fabíola Gomes de Araújo
* Fernanda Vieira de Barros Almeida
* Flávio Leonardo Cavalcanti Cordeiro
* Francisco do Nascimento Júnior
* Homero Sampaio Cavalcanti
* Ivan Sérgio da Silva Júnior
* José Edmar de Siqueira
* José Thiago Tenório Lopes
* Kássia Regina Silvestre de Albuquerque
* Leonardo Luiz Vieira da Silva
* Márcio Roberto Batista da Silva
* Maria de Fátima Sampaio Leite
* Micaela Maria Coelho de Araújo
* Nelson Mendonça de Carvalho
* Newton Morais e Silva
* Pedro Alexandre Santos da Silva Filho
* Rafael Corrêa Lima e Silva
* Rafael Francisco Pinto
* Rafael Koury Monteiro
* Rafael Palermo de Araújo
* Raphael Veras Rossiter
* Roberto Sobreira Barbalho
* Rodrigo Avellar Silveira
* Rosana Carvalho Barbosa
* Sávio Luiz de Andrade Cavalcante
* Tai Mu Shih
* Thiago Augusto Souza do Nascimento
* Thúlio dos Santos Lins de Araújo
* Tiago Moreno Rodrigues
* Vivianne Barbosa Sousa
*
* Este programa é software livre; você pode redistribuí-lo e/ou
* modificá-lo sob os termos de Licença Pública Geral GNU, conforme
* publicada pela Free Software Foundation; versão 2 da
* Licença.
* Este programa é distribuído na expectativa de ser útil, mas SEM
* QUALQUER GARANTIA; sem mesmo a garantia implícita de
* COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER PROPÓSITO EM
* PARTICULAR. Consulte a Licença Pública Geral GNU para obter mais
* detalhes.
* Você deve ter recebido uma cópia da Licença Pública Geral GNU
* junto com este programa; se não, escreva para Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
* 02111-1307, USA.
*/
package com.br.ipad.gsanas.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.br.ipad.gsanas.ui.Guide;

public class Util {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static SimpleDateFormat dateFormatFileName = new SimpleDateFormat("ddMMyyyyHHmmss");
	private static SimpleDateFormat dateOnlyFormatFileName = new SimpleDateFormat("ddMMyyyy");
	private static SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd/MM/yyyy");
    
	/**
	 * Função usada para converter um Date para String
	 * 
	 * @author Thúlio Araújo
	 * @param date
	 * @return String
	 */
	public static String convertDateToDateStr(Date date){
		String dateStr = dateFormat.format(date);
		return dateStr;
	}
	
	/**
	 * Função usada para converter um Date para String em formato de nome de arquivo 
	 * 
	 * @author Fernanda Almeida
	 * @param date
	 * @return String
	 */
	public static String convertDateToDateStrFile(){
		Date date = new Date();
		String dateStr = dateFormatFileName.format(date);
		return dateStr;
	}
	
	/**
	 * Função usada para converter um Date para String em formato de nome de arquivo 
	 * 
	 * @author Fernanda Almeida
	 * @param date
	 * @return String
	 */
	public static String convertDateToDateOnlyStrFile(){
		Date date = new Date();
		String dateStr = dateOnlyFormatFileName.format(date);
		return dateStr;
	}
	
	 /**
     * Verifica se o valor da String.trim() veio como null ou como
     * Constantes.NULO_STRING, setando como Constantes.NULO_STRING caso
     * verdadadeiro
     * 
     * @param valor
     * @return
     */
    public static String verificarNuloString(String valor) {
		if (valor == null || valor.trim().equals(Constants.NULO_STRING) || valor.trim().equals("null")) {
		    return Constants.NULO_STRING;
		} else {
		    return valor.trim();
		}
    }
	
	/**
	 * Função usada para converter uma String para um Date
	 * 
	 * @author Thúlio Araújo
	 * @param dateStr
	 * @return Date
	 */
	public static Date convertDateStrToDate(String dateStr){
		Date date = null;
		try {
			date = (Date)dateFormat.parse(dateStr);
		} catch (ParseException e) {
			
			Log.e( Constants.CATEGORY , e.getMessage());
		}
		return date;
	}
	
	public static Date convertDateStrToDate1(String dateStr){
		Date date = null;
		try {
			date = (Date)dateOnlyFormat.parse(dateStr);
		} catch (ParseException e) {
			
			Log.e( Constants.CATEGORY , e.getMessage());
		}
		return date;
	}
	
	/**
	 * Função usada para retornar a data e hora atual
	 * 
	 * @author Thúlio Araújo
	 * @return Date
	 */
	public static Date getCurrentDateTime(){
		Date date = new Date();
//		Calendar gregorianCalendar = GregorianCalendar.getInstance();
//		date = gregorianCalendar.getTime();
		return date;
	}
	
	/**
	 * Método que verifica se a string passada já tem casa decimal
	 * 
	 * 
	 * @param data
	 * @autor Fernanda Almeida
	 * @return
	 */

	public static boolean verificaSeBigDecimalQtt(String valor) { 
		double maxVal = 9999.99;
		boolean temCasaDecimal = false;
		if(valor.equals(".") || valor.equals("-") ||  valor.equals("") || valor.equals("0")){
			return false;
		}
		if(Double.parseDouble(valor) > maxVal){
			return false;
		}
		// Validação para NUMERIC(6,2)
		if(valor.matches("\\d{1,4}.?[0-9]{0,2}") && Double.parseDouble(valor) > 0){
			temCasaDecimal = true;
		}
		return temCasaDecimal;
		
	}
	
	/**
	 * Método que verifica se a string passada já tem casa decimal
	 * 
	 * 
	 * @param data
	 * @autor Fernanda Almeida
	 * @return
	 */

	public static boolean verificaSeBigDecimal(String valor) {

		boolean temCasaDecimal = false;
		if(valor.equals(".") || valor.equals("-") ||  valor.equals("") || valor.equals("0")){
			return false;
		}
		BigDecimal valorBig = new BigDecimal(valor);
		if(valorBig.intValue() > 0){	
			temCasaDecimal = true;
		}
		return temCasaDecimal;
	}
	
	 /**
     * Verifica se o valor da String.trim() veio como null ou como
     * Constantes.NULO_STRING, setando como Constantes.NULO_INT caso
     * verdadadeiro
     * 
     * @param valor
     * @return
     */
    public static Integer verificarNuloInt(String valor) {
	if (valor == null || valor.trim().equals(Constants.NULO_STRING)) {
	    return Constants.NULO_INT;
	} else {
	    return Integer.parseInt(valor.trim());
	}
    }
	    
    public static Date getData(String data) {

    	if (data.equals(null)) {
    	    return null;
    	} else {
    	    Calendar calendario = Calendar.getInstance();
    	    calendario.set(Calendar.YEAR, Integer.valueOf(data.substring(0, 2)).intValue());
    	    calendario.set(Calendar.MONTH, Integer.valueOf(data.substring(3, 5)).intValue() - 1);
    	    calendario.set(Calendar.DAY_OF_MONTH, Integer.valueOf(data.substring(6, 10)).intValue());
    	    calendario.set(Calendar.HOUR, 0);
    	    calendario.set(Calendar.HOUR_OF_DAY, 0);
    	    calendario.set(Calendar.MINUTE, 0);
    	    calendario.set(Calendar.SECOND, 0);
    	    calendario.set(Calendar.MILLISECOND, 0);

    	    return new Date(calendario.getTime().getTime());
    	}

    }
    
    /**
     * Transforma uma string de ISO-8859-1 para UTF-8
     * 
     * @param string
     * @return
     */
	public static String reencode(String string) {

		String retorno = "";

		try {
			retorno = new String(string.getBytes("ISO-8859-1"));// , "UTF-8" );
		} catch (Exception e) {
			retorno = string;
		}

		return retorno;
	}
	
	public static String getIMEI( Context context ){
		TelephonyManager telephony;

		// == Adquirindo uma referência ao Sistema de Gerenciamento do Telefone
		telephony = (TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE );

		if(telephony.getDeviceId() == null){
			return "0";
		}
		// == Método que retorna o IMEI
		return telephony.getDeviceId();		
	}	
	
	/**
     * Retorna um Bitmap de modo que nao utilize muita memoria.
     * Previne o memoryOutOfBounds     
     */
	public static Bitmap decodeFile(File f){
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	        //The new size we want to scale to
	        final int REQUIRED_SIZE=70;

	        //Find the correct scale value. It should be the power of 2.
	        int width_tmp=o.outWidth, height_tmp=o.outHeight;
	        int scale=1;
	        while(true){
	            if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                break;
	            width_tmp/=2;
	            height_tmp/=2;
	            scale*=2;
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } catch (FileNotFoundException e) {}
	    return null;
	}
	
	/**
	 * 
	 * Adiciona um pipe ao final do objeto passado
	 * 
	 * @param  String a ter o pipe adicionado
	 * @return 
	 */
	public static String stringPipe( Object obj ){
		return obj + "|";
	}
	
	
	/**
	 * Converte a data passada em string
	 * 
	 * @author: Thiago Toscano, Thiago Toscano
	 * @date: 20/03/2006, 20/03/2006
	 * 
	 * @param data
	 *            Descrição do parâmetro
	 * @return Descrição do retorno
	 */
	public static String formatDate(Date data) {
		
		String retorno = "";
		if (data != null) { // 1
			Calendar dataCalendar = new GregorianCalendar();
			StringBuffer dataBD = new StringBuffer();

			dataCalendar.setTime(data);

			if (dataCalendar.get(Calendar.DAY_OF_MONTH) > 9) {
				dataBD.append(dataCalendar.get(Calendar.DAY_OF_MONTH) + "/");
			} else {
				dataBD.append("0" + dataCalendar.get(Calendar.DAY_OF_MONTH)
						+ "/");
			}

			// Obs.: Janeiro no Calendar é mês zero
			if ((dataCalendar.get(Calendar.MONTH) + 1) > 9) {
				dataBD.append(dataCalendar.get(Calendar.MONTH) + 1 + "/");
			} else {
				dataBD.append("0" + (dataCalendar.get(Calendar.MONTH) + 1)
						+ "/");
			}

			dataBD.append(dataCalendar.get(Calendar.YEAR));
			retorno = dataBD.toString();
		}
		
		return retorno;
	}
	
	
/*	public static String convertImageToBase64( String path ){
		Bitmap bm = BitmapFactory.decodeFile(path);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
		byte[] b = baos.toByteArray(); 		
		
		return Base64.encodeBytes( b );

	}*/
	
	/**
	 * 
	 * Deleta as pastas e arquivos filhos de gsanas	
	 * 
	 * @param  File da pasta gsanas no SDCARD
	 * @return 
	 */
	public static void deletePhotoFolder(File fileOrDirectory){	
		// Deleta as pastas diferente de 'carregamento'
		if(!fileOrDirectory.getName().equals("carregamento") && !fileOrDirectory.getName().equals("return")
				&& !fileOrDirectory.getName().equals("version")){
			if (fileOrDirectory.isDirectory()){
		        for (File child : fileOrDirectory.listFiles()){
		        	deletePhotoFolder(child);
		        }
			}
		    fileOrDirectory.delete();
		}
	}	
	
	/**
	 * < <Descrição do método>>
	 * 
	 * @param data
	 *            Descrição do parâmetro
	 * @return Descrição do retorno
	 */
	public static String formatarDataComHora(Date data) {
		StringBuffer dataBD = new StringBuffer();

		if (data != null) {
			Calendar dataCalendar = new GregorianCalendar();

			dataCalendar.setTime(data);

			if (dataCalendar.get(Calendar.DAY_OF_MONTH) > 9) {
				dataBD.append(dataCalendar.get(Calendar.DAY_OF_MONTH) + "/");
			} else {
				dataBD.append("0" + dataCalendar.get(Calendar.DAY_OF_MONTH)
						+ "/");
			}

			// Obs.: Janeiro no Calendar é mês zero
			if ((dataCalendar.get(Calendar.MONTH) + 1) > 9) {
				dataBD.append(dataCalendar.get(Calendar.MONTH) + 1 + "/");
			} else {
				dataBD.append("0" + (dataCalendar.get(Calendar.MONTH) + 1)
						+ "/");
			}

			dataBD.append(dataCalendar.get(Calendar.YEAR));

			dataBD.append(" ");

			if (dataCalendar.get(Calendar.HOUR_OF_DAY) > 9) {
				dataBD.append(dataCalendar.get(Calendar.HOUR_OF_DAY));
			} else {
				dataBD.append("0" + dataCalendar.get(Calendar.HOUR_OF_DAY));
			}

			dataBD.append(":");

			if (dataCalendar.get(Calendar.MINUTE) > 9) {
				dataBD.append(dataCalendar.get(Calendar.MINUTE));
			} else {
				dataBD.append("0" + dataCalendar.get(Calendar.MINUTE));
			}

			dataBD.append(":");

			if (dataCalendar.get(Calendar.SECOND) > 9) {
				dataBD.append(dataCalendar.get(Calendar.SECOND));
			} else {
				dataBD.append("0" + dataCalendar.get(Calendar.SECOND));
			}
		}

		return dataBD.toString();
	}
	
	/**
	 * Método que recebe uma string e converte para BigDecimal
	 * casas decimais
	 * 
	 * @param data
	 * @autor Thúlio Araújo
	 * @date 29/09/2011
	 * @return BigDecimal
	 */
	public static BigDecimal convertStringToBigDecimal(String value) {
		BigDecimal numberFormated = null;
		try {
			numberFormated = new BigDecimal(value);
		} catch (NumberFormatException e) {
			numberFormated = null;
		}
		return numberFormated;
	}
	
	/**
     * @author Magno Gouveia
     * @since 15/09/2011
     * @param line
     *            linha do arquivo que deve ser parseada
     * @return um array com os campos do objeto
     */
    public static Vector<String> split(String line) {
        Vector<String> lines = new Vector<String>();

        char[] chars = line.toCharArray();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != '|') {
                sb.append(chars[i]);
            } else {
                lines.add(sb.toString());
                sb = new StringBuilder();
            }
        }

        return lines;
    }
	
	/**
	 * Verifica se duas datas são iguais
	 * 
	 * @author Thúlio Araújo
	 * @date 06/10/2011
	 * 
	 * @param primeiraData
	 *            <Descrição>
	 * @param segundaData
	 *            <Descrição>
	 * @return retorno
	 */
	public static boolean isEqualsDates(Date firstDate, Date secondDate) {

		boolean sucess = false;

		Calendar d1 = Calendar.getInstance();
		Calendar d2 = Calendar.getInstance();

		d1.setTime(firstDate);
		d2.setTime(secondDate);

		d1.set(Calendar.HOUR_OF_DAY, 0);
		d1.set(Calendar.MINUTE, 0);
		d1.set(Calendar.SECOND, 0);
		d1.set(Calendar.MILLISECOND, 0);

		d2.set(Calendar.HOUR_OF_DAY, 0);
		d2.set(Calendar.MINUTE, 0);
		d2.set(Calendar.SECOND, 0);
		d2.set(Calendar.MILLISECOND, 0);

		if (d1.getTime().equals(d2.getTime())) {
			sucess = true;
		}

		return sucess;
	}

	/**
	 * Método para retornar o valor do InputStream para o E71 e o E5
	 * 
	 * @param input
	 * @return string de retorno com o valor
	 * @throws IOException
	 */
	public static String getValorRespostaInputStream(InputStream input) throws IOException {
		char valor = ' ';
		try {

			// ---INICIO E5
			valor = (char) input.read();
			// ---FIM E5

		} catch (Exception e) {
			// ---INICIO E71
			InputStreamReader isr = new InputStreamReader(input);
			valor = (char) isr.read();
			// ---FIM E71

		}

		return String.valueOf(valor);
	}
	
	public static void cancelaAlarmes(){
		    	
    	// finaliza o alarm comunication
    	PendingIntent pCommunication = Guide.returnPendingIntent();
    	AlarmManager alarmCommunication = Guide.returnAlarm();
    	if(alarmCommunication != null){
    		alarmCommunication.cancel(pCommunication);
    	}
    	
    	Guide.resetAlarm();
	}
	
	public static byte[] empacotarParametros(ArrayList<Object> parameter) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		byte[] resposta = null;

		parameter.trimToSize();

		// escreve os dados no OutputStream
		if (parameter != null) {
			int tamanho = parameter.size();
			for (int i = 0; i < tamanho; i++) {
				Object param = parameter.get(i);
				if (param instanceof Byte) {
					dos.writeByte(((Byte) param).byteValue());
				} else if (param instanceof Integer) {
					dos.writeInt(((Integer) param).intValue());
				} else if (param instanceof Short) {
					dos.writeShort(((Short) param).shortValue());					
				} else if (param instanceof Long) {
					dos.writeLong(((Long) param).longValue());
				} else if (param instanceof String) {
					dos.writeUTF((String) param);
				} else if (param instanceof byte[]) {
					dos.write((byte[]) param);
				}
			}
		}

		// pega os dados enpacotados
		resposta = baos.toByteArray();

		if (dos != null) {
			dos.close();
			dos = null;
		}
		if (baos != null) {
			baos.close();
			baos = null;
		}

		// retorna o array de bytes
		return resposta;
	}
	
	public static BigDecimal  getDoubleBanco (Cursor cursor, String columnName, int columnIndex){
		
		if (! cursor.isNull(cursor.getColumnIndex(columnName)) ){
			return BigDecimal.valueOf(cursor.getDouble(columnIndex));
		}else{
			return null;
		}
		
	}

}
