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
import com.br.ipad.gsanas.model.Floor;
import com.br.ipad.gsanas.model.Floor.Floors;

import com.br.ipad.gsanas.ui.R;
import com.br.ipad.gsanas.util.Constants;

/**
 * [GSANAS] Repository - Floor
 * 
 * @author Amélia Pessoa
 * @since 26/04/2012
 */
public class FloorRepository extends BasicRepository implements IFloorRepository {
	
	private static FloorRepository instance;
	public static final String TABLE_NAME = "FLOOR";
	
	public void resetInstance(){
		instance = null;
	}

	public static FloorRepository getInstance(){
		if(instance==null){
			instance = new FloorRepository();
		}		
		return instance;
	}
	
	public Floor getFloorById(int id) throws RepositoryException {
		
		Cursor cursor = null;
		
		Floor floor = null;
		
		try {
			cursor = db.query(TABLE_NAME, Floor.getColumns(), Floors.ID + " = " + id, null,
					null, null, null, null);
			
			if (cursor.moveToFirst()) {
				
				floor = new Floor();
				
				int codigo = cursor.getColumnIndex(Floors.ID);
				int description = cursor.getColumnIndex(Floors.DESCRIPTION);
				int index = cursor.getColumnIndex(Floors.INDEXFLOORKIND);
				int codeGSAN = cursor.getColumnIndex(Floors.CODEFLOORGSAN);
				
				floor.setId(cursor.getInt(codigo));
				floor.setDescription(cursor.getString(description));
				floor.setIndexFloorKind(cursor.getInt(index));
				floor.setCodeFloorGSAN(cursor.getInt(codeGSAN));
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
		return floor;
	}	

public ArrayList<Floor> getAllFloorsByFloorKind(int floorKind) throws RepositoryException {
		
		Cursor cursor = null;
		
		ArrayList<Floor> floors;
		try {
			floors = new ArrayList<Floor>();
			cursor = db.query(TABLE_NAME, Floor.getColumns(), Floors.INDEXFLOORKIND + " = " + floorKind, null,
				null, null, null, null);
			
			if (cursor.moveToFirst()) {
				
				int codigo = cursor.getColumnIndex(Floors.ID);
				int description = cursor.getColumnIndex(Floors.DESCRIPTION);
				int index = cursor.getColumnIndex(Floors.INDEXFLOORKIND);
				int codeGSAN = cursor.getColumnIndex(Floors.CODEFLOORGSAN);
				
				do {
					Floor floor = new Floor();				
					
					floor.setId(cursor.getInt(codigo));
					floor.setDescription(cursor.getString(description));
					floor.setIndexFloorKind(cursor.getInt(index));
					floor.setCodeFloorGSAN(cursor.getInt(codeGSAN));
					
					floors.add(floor);
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
		return floors;
	}

	public ArrayList<Floor> getAllFloors() throws RepositoryException {
		
		Cursor cursor = null;
		
		ArrayList<Floor> floors;
		try {
			floors = new ArrayList<Floor>();
			cursor = db.query(TABLE_NAME, Floor.getColumns(), null , null,
					null, null, null, null);
			
			if (cursor.moveToFirst()) {
				
				int codigo = cursor.getColumnIndex(Floors.ID);
				int description = cursor.getColumnIndex(Floors.DESCRIPTION);
				int index = cursor.getColumnIndex(Floors.INDEXFLOORKIND);
				int codeGSAN = cursor.getColumnIndex(Floors.CODEFLOORGSAN);
				
				do {
					Floor floor = new Floor();				
					
					floor.setId(cursor.getInt(codigo));
					floor.setDescription(cursor.getString(description));
					floor.setIndexFloorKind(cursor.getInt(index));
					floor.setCodeFloorGSAN(cursor.getInt(codeGSAN));
					
					floors.add(floor);
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
		return floors;
	}
	
	public void insertFloor(Floor floor) throws RepositoryException {
		ContentValues values = new ContentValues();
		values.put(Floors.ID, floor.getId());
		values.put(Floors.DESCRIPTION, floor.getDescription());
		values.put(Floors.INDEXFLOORKIND, floor.getIndexFloorKind());
		values.put(Floors.CODEFLOORGSAN, floor.getCodeFloorGSAN());
		
		try {
			db.insert(TABLE_NAME, "", values);
			
		} catch (SQLException e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error_insert_record));
		}		
		
	}

	public void updateFloor(Floor floor) throws RepositoryException {
		ContentValues values = new ContentValues();
		values.put(Floors.DESCRIPTION, floor.getDescription());
		values.put(Floors.INDEXFLOORKIND, floor.getIndexFloorKind());
		values.put(Floors.CODEFLOORGSAN, floor.getCodeFloorGSAN());

		String _id = String.valueOf(floor.getId());

		String where = Floors.ID + "=?";
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

	public void removeFloor(Floor floor) throws RepositoryException {
		String _id = String.valueOf(floor.getId());

		String where = Floors.ID + "=?";
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
