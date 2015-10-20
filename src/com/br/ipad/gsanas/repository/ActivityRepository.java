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
package com.br.ipad.gsanas.repository;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.br.ipad.gsanas.exception.RepositoryException;
import com.br.ipad.gsanas.model.Activit;
import com.br.ipad.gsanas.model.Activit.Activities;
import com.br.ipad.gsanas.ui.R;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

/**
 * [GSANAS] Repository - Activit
 * 
 * @author Fernanda Almeida
 * @since 06/07/2011
 */
public class ActivityRepository extends BasicRepository implements IActivityRepository {
	
	private static ActivityRepository instance;
	public static final String TABLE_NAME = "ACTIVITY";
	
	public void resetInstance(){
		instance = null;
	}

	public static ActivityRepository getInstance(){
		if(instance==null){
			instance = new ActivityRepository();
		}		
		return instance;
	}
	
	public Activit getActivityById(int id) throws RepositoryException {
		
		Cursor cursor = null;
		
		Activit activit = null;
		
		try {
			cursor = db.query(TABLE_NAME, Activit.getColumns(), Activities.ID + " = " + id, null,
					null, null, null, null);
			
			if (cursor.moveToFirst()) {
				
				activit = new Activit();
				
				int codigo = cursor.getColumnIndex(Activities.ID);
				int descriptionActivity = cursor.getColumnIndex(Activities.DESCRIPTIONACTIVITY);
				int descriptionAbbreviated = cursor.getColumnIndex(Activities.DESCRIPTIONABBREVIATED);
				int lastChange = cursor.getColumnIndex(Activities.LASTCHANGE);
				
				activit.setId(cursor.getInt(codigo));
				activit.setDescriptionActivity(cursor.getString(descriptionActivity));
				activit.setDescriptionAbbreviated(cursor.getString(descriptionAbbreviated));
				activit.setLastChange(cursor.getString(lastChange));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error));
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return activit;
	}	

	public ArrayList<Activit> getAllActivities() throws RepositoryException {
		
		Cursor cursor = null;
		
		ArrayList<Activit> activits;
		try {
			activits = new ArrayList<Activit>();
			cursor = db.query(TABLE_NAME, Activit.getColumns(), null , null,
					null, null, null, null);
			
			if (cursor.moveToFirst()) {
				int codigo = cursor.getColumnIndex(Activities.ID);
				int descriptionActivity = cursor.getColumnIndex(Activities.DESCRIPTIONACTIVITY);
				int descriptionAbbreviated = cursor.getColumnIndex(Activities.DESCRIPTIONABBREVIATED);
				int lastChange = cursor.getColumnIndex(Activities.LASTCHANGE);
				
				
				do {
					Activit activit = new Activit();
					
					activit.setId(cursor.getInt(codigo));
					activit.setDescriptionActivity(cursor.getString(descriptionActivity));
					activit.setDescriptionAbbreviated(cursor.getString(descriptionAbbreviated));
					activit.setLastChange(cursor.getString(lastChange));
					
					activits.add(activit);
				} while (cursor.moveToNext());
				
			}
		} catch (Exception e) {
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error_search_record));
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return activits;
	}
	
	public void insertActivity(Activit activit) throws RepositoryException {
		ContentValues values = new ContentValues();
		values.put(Activities.ID, activit.getId());
		values.put(Activities.DESCRIPTIONACTIVITY, activit.getDescriptionActivity());
		values.put(Activities.DESCRIPTIONABBREVIATED, activit.getDescriptionAbbreviated());		
		String date = Util.convertDateToDateStr(Util.getCurrentDateTime());
		values.put(Activities.LASTCHANGE, date);
		
		try {
			db.insert(TABLE_NAME, "", values);
			
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error_insert_record));
		}		
		
	}

	public void updateActivity(Activit activit) throws RepositoryException {
		ContentValues values = new ContentValues();
		values.put(Activities.DESCRIPTIONACTIVITY, activit.getDescriptionActivity());
		values.put(Activities.DESCRIPTIONABBREVIATED, activit.getDescriptionAbbreviated());
		String date = Util.convertDateToDateStr(Util.getCurrentDateTime());
		values.put(Activities.LASTCHANGE, date);

		String _id = String.valueOf(activit.getId());

		String where = Activities.ID + "=?";
		String[] whereArgs = new String[] { _id };

		try {
			db.update(TABLE_NAME, values, where, whereArgs);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error));
		}
		
	}

	public void removeActivity(Activit activit) throws RepositoryException {
		String _id = String.valueOf(activit.getId());

		String where = Activities.ID + "=?";
		String[] whereArgs = new String[] { _id };

		try {
			db.delete( TABLE_NAME, where, whereArgs);
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error));
		}
	}

	

}
