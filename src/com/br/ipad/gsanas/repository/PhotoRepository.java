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
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.br.ipad.gsanas.exception.RepositoryException;
import com.br.ipad.gsanas.model.Photo;
import com.br.ipad.gsanas.model.Photo.Photos;
import com.br.ipad.gsanas.ui.R;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

/**
 * [GSANAS] Repository - Photo
 * 
 * @author Thúlio Araújo
 * @since 06/07/2011
 */
public class PhotoRepository extends BasicRepository implements IPhotoRepository {
	
	private static PhotoRepository instance;	
	
	public static final String TABLE_NAME = "PHOTO";
	
	public void resetInstance(){
		instance = null;
	}

	public static PhotoRepository getInstance(){
		if ( instance == null ){
			instance =  new PhotoRepository();
		} 
		return instance;
	}
	
	public Photo getPhotoById(int id) throws RepositoryException {
		Cursor cursor = null;
		Photo photo = null;

		try {
			cursor = db.query(TABLE_NAME, Photo.getColumns(), Photos.ID + "=" + id , null,
					null, null, null, null);

			if (cursor.moveToFirst()) {

				photo = new Photo();

				int idxId = cursor.getColumnIndex(Photos.ID);
				int idxServiceOrder = cursor.getColumnIndex(Photos.SERVICEORDER);
				int idxPhotoType = cursor.getColumnIndex(Photos.PHOTOTYPE);
				int idxDescriptionObservation = cursor.getColumnIndex(Photos.DESCRIPTIONOBSERVATION);
				int idxPath = cursor.getColumnIndex(Photos.PATH);
				int idxLastChange = cursor.getColumnIndex(Photos.LASTCHANGE);
				int cordinateX = cursor.getColumnIndex(Photos.CORDINATEX);
				int cordinateY = cursor.getColumnIndex(Photos.CORDINATEY);
				int idFlagSent = cursor.getColumnIndex(Photos.FLAGSENT);
				
				photo.setId(cursor.getInt(idxId));
				photo.setServiceOrder(ServiceOrderRepository.getInstance().getServiceOrderById(cursor.getInt(idxServiceOrder)));
				photo.setPhotoType(PhotoTypeRepository.getInstance().getPhotoTypeById(cursor.getInt(idxPhotoType)));
				photo.setDescriptionObservation(cursor.getString(idxDescriptionObservation));
				photo.setPath(cursor.getString(idxPath));				
				photo.setCordinateX(cursor.getDouble(cordinateX));		
				photo.setCordinateY(cursor.getDouble(cordinateY));		
				photo.setFlagSent(cursor.getInt(idFlagSent));
				Date date = new Date();
				String dateStr = cursor.getString(idxLastChange);
				date = Util.convertDateStrToDate(dateStr);
				photo.setLastChange(date);
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
		return photo;
	}
	
	public ArrayList<Photo> getPhotoByFk( Integer idServiceOrder ) throws RepositoryException {
		Cursor cursor = null;

		ArrayList<Photo> photos;
		try {
			photos = new ArrayList<Photo>();
			cursor = db.query(TABLE_NAME, Photo.getColumns(), Photos.SERVICEORDER + "=" + idServiceOrder , null,
				null, null, null, null);

			if (cursor.moveToFirst()) {
				int idxId = cursor.getColumnIndex(Photos.ID);
				int idxServiceOrder = cursor.getColumnIndex(Photos.SERVICEORDER);
				int idxPhotoType = cursor.getColumnIndex(Photos.PHOTOTYPE);
				int idxDescriptionObservation = cursor.getColumnIndex(Photos.DESCRIPTIONOBSERVATION);
				int idxPath = cursor.getColumnIndex(Photos.PATH);
				int idxLastChange = cursor.getColumnIndex(Photos.LASTCHANGE);
				int cordinateX = cursor.getColumnIndex(Photos.CORDINATEX);
				int cordinateY = cursor.getColumnIndex(Photos.CORDINATEY);
				int idFlagSent = cursor.getColumnIndex(Photos.FLAGSENT);

				do {
					Photo photo = new Photo();
					photo.setId(cursor.getInt(idxId));
					photo.setServiceOrder(ServiceOrderRepository.getInstance().getServiceOrderById(cursor.getInt(idxServiceOrder)));
					photo.setPhotoType(PhotoTypeRepository.getInstance().getPhotoTypeById(cursor.getInt(idxPhotoType)));
					photo.setDescriptionObservation(cursor.getString(idxDescriptionObservation));
					photo.setPath(cursor.getString(idxPath));				
					Date date = new Date();
					String dateStr = cursor.getString(idxLastChange);	
					photo.setCordinateX(cursor.getDouble(cordinateX));		
					photo.setCordinateY(cursor.getDouble(cordinateY));	
					date = Util.convertDateStrToDate(dateStr);
					photo.setLastChange(date);
					photo.setFlagSent(cursor.getInt(idFlagSent));
					photos.add(photo);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error_search_record));
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return photos;
	}
	
	public Photo getPhotoByPath( Integer idServiceOrder, String path ) throws RepositoryException {
		Cursor cursor = null;
		Photo photo = null;
		
		try {
			cursor = db.query(TABLE_NAME, Photo.getColumns(), Photos.SERVICEORDER + "=" + idServiceOrder + " and " + 
				Photos.PATH + " LIKE '" + path + "'", null,
				null, null, null, null);

			if (cursor.moveToFirst()) {

				photo = new Photo();

				int idxId = cursor.getColumnIndex(Photos.ID);
				int idxServiceOrder = cursor.getColumnIndex(Photos.SERVICEORDER);
				int idxPhotoType = cursor.getColumnIndex(Photos.PHOTOTYPE);
				int idxDescriptionObservation = cursor.getColumnIndex(Photos.DESCRIPTIONOBSERVATION);
				int idxPath = cursor.getColumnIndex(Photos.PATH);
				int idxLastChange = cursor.getColumnIndex(Photos.LASTCHANGE);
				int cordinateX = cursor.getColumnIndex(Photos.CORDINATEX);
				int cordinateY = cursor.getColumnIndex(Photos.CORDINATEY);
				int idFlagSent = cursor.getColumnIndex(Photos.FLAGSENT);
				
				photo.setId(cursor.getInt(idxId));
				photo.setServiceOrder(ServiceOrderRepository.getInstance().getServiceOrderById(cursor.getInt(idxServiceOrder)));
				photo.setPhotoType(PhotoTypeRepository.getInstance().getPhotoTypeById(cursor.getInt(idxPhotoType)));
				photo.setDescriptionObservation(cursor.getString(idxDescriptionObservation));
				photo.setPath(cursor.getString(idxPath));				
				Date date = new Date();
				String dateStr = cursor.getString(idxLastChange);
				photo.setCordinateX(cursor.getDouble(cordinateX));		
				photo.setCordinateY(cursor.getDouble(cordinateY));	 
				date = Util.convertDateStrToDate(dateStr);
				photo.setLastChange(date);
				photo.setFlagSent(cursor.getInt(idFlagSent));
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
		return photo;
	}

	public ArrayList<Photo> getAllPhoto() throws RepositoryException {
		Cursor cursor = null;

		ArrayList<Photo> photos;
		try {
			photos = new ArrayList<Photo>();
			cursor = db.query(TABLE_NAME, Photo.getColumns(), null , null,
					null, null, null, null);

			if (cursor.moveToFirst()) {
				int idxId = cursor.getColumnIndex(Photos.ID);
				int idxServiceOrder = cursor.getColumnIndex(Photos.SERVICEORDER);
				int idxPhotoType = cursor.getColumnIndex(Photos.PHOTOTYPE);
				int idxDescriptionObservation = cursor.getColumnIndex(Photos.DESCRIPTIONOBSERVATION);
				int idxPath = cursor.getColumnIndex(Photos.PATH);
				int idxLastChange = cursor.getColumnIndex(Photos.LASTCHANGE);
				int cordinateX = cursor.getColumnIndex(Photos.CORDINATEX);
				int cordinateY = cursor.getColumnIndex(Photos.CORDINATEY);
				int idFlagSent = cursor.getColumnIndex(Photos.FLAGSENT);

				do {
					Photo photo = new Photo();
					photo.setId(cursor.getInt(idxId));
					photo.setServiceOrder(ServiceOrderRepository.getInstance().getServiceOrderById(cursor.getInt(idxServiceOrder)));
					photo.setPhotoType(PhotoTypeRepository.getInstance().getPhotoTypeById(cursor.getInt(idxPhotoType)));
					photo.setDescriptionObservation(cursor.getString(idxDescriptionObservation));
					photo.setPath(cursor.getString(idxPath));		
					photo.setCordinateX(cursor.getDouble(cordinateX));		
					photo.setCordinateY(cursor.getDouble(cordinateY));		
					Date date = new Date();
					String dateStr = cursor.getString(idxLastChange);
					date = Util.convertDateStrToDate(dateStr);
					photo.setLastChange(date);
					photo.setFlagSent(cursor.getInt(idFlagSent));
					photos.add(photo);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error_search_record));
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return photos;
	}

	public void insertPhoto(Photo photo) throws RepositoryException {
		ContentValues values = new ContentValues();
		values.put(Photos.SERVICEORDER, photo.getServiceOrder().getId());
		values.put(Photos.PHOTOTYPE, photo.getPhotoType().getId());
		values.put(Photos.DESCRIPTIONOBSERVATION, photo.getDescriptionObservation());
		values.put(Photos.PATH, photo.getPath());
		String date = Util.convertDateToDateStr(Util.getCurrentDateTime());
		values.put(Photos.LASTCHANGE, date);
		values.put(Photos.CORDINATEX,  photo.getCordinateX());
		values.put(Photos.CORDINATEY,  photo.getCordinateY());
		values.put(Photos.FLAGSENT, Constants.NO);

		try {
			db.insert(TABLE_NAME, "", values);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			Log.e( Constants.CATEGORY , sqe.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error_insert_record));
		}
	}

	public void updatePhoto(Photo photo) throws RepositoryException {
		ContentValues values = new ContentValues();
		if(photo.getServiceOrder()!=null){
			values.put(Photos.SERVICEORDER, photo.getServiceOrder().getId());
		}
		if(photo.getPhotoType()!=null){
			values.put(Photos.PHOTOTYPE, photo.getPhotoType().getId());
		}
		values.put(Photos.DESCRIPTIONOBSERVATION, photo.getDescriptionObservation());
		values.put(Photos.PATH, photo.getPath());
		values.put(Photos.CORDINATEX,  photo.getCordinateX());
		values.put(Photos.CORDINATEY,  photo.getCordinateY());
		String date = Util.convertDateToDateStr(Util.getCurrentDateTime());
		values.put(Photos.LASTCHANGE, date);
		values.put(Photos.FLAGSENT, photo.getFlagSent());

		String _id = String.valueOf(photo.getId());

		String where = Photos.ID + "=?";
		String[] whereArgs = new String[] { _id };

		try {
			db.update(TABLE_NAME, values, where, whereArgs);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			Log.e( Constants.CATEGORY , sqe.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error));
		}
	}

	public void removePhoto(Photo photo) throws RepositoryException {
		String _id = String.valueOf(photo.getId());

		String where = Photos.ID + "=?";
		String[] whereArgs = new String[] { _id };

		try {
			db.delete( TABLE_NAME, where, whereArgs);
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			Log.e( Constants.CATEGORY , sqe.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error));
		}
	}
	
	/**
	 * Retorna as fotos não enviadas
	 * 
	 * @date 14/11/2012
	 * @author Fernanda
	 * @return ArrayList<Photo>
	 * @throws RepositoryException
	 */
	public ArrayList<Photo> getAllPhotosNotSent() throws RepositoryException {
		Cursor cursor = null;

		ArrayList<Photo> photos;
		try {
			photos = new ArrayList<Photo>();
			cursor = db.query(TABLE_NAME, Photo.getColumns(), Photos.FLAGSENT+ "="+ Constants.NO , null,
					null, null, null, null);

			if (cursor.moveToFirst()) {
				int idxId = cursor.getColumnIndex(Photos.ID);
				int idxServiceOrder = cursor.getColumnIndex(Photos.SERVICEORDER);
				int idxPhotoType = cursor.getColumnIndex(Photos.PHOTOTYPE);
				int idxDescriptionObservation = cursor.getColumnIndex(Photos.DESCRIPTIONOBSERVATION);
				int idxPath = cursor.getColumnIndex(Photos.PATH);
				int idxLastChange = cursor.getColumnIndex(Photos.LASTCHANGE);
				int idFlagSent = cursor.getColumnIndex(Photos.FLAGSENT);
				int cordinateX = cursor.getColumnIndex(Photos.CORDINATEX);
				int cordinateY = cursor.getColumnIndex(Photos.CORDINATEY);
				 

				do {
					Photo photo = new Photo();
					photo.setId(cursor.getInt(idxId));
					photo.setServiceOrder(ServiceOrderRepository.getInstance().getServiceOrderById(cursor.getInt(idxServiceOrder)));
					photo.setPhotoType(PhotoTypeRepository.getInstance().getPhotoTypeById(cursor.getInt(idxPhotoType)));
					photo.setDescriptionObservation(cursor.getString(idxDescriptionObservation));
					photo.setPath(cursor.getString(idxPath));				
					Date date = new Date();
					String dateStr = cursor.getString(idxLastChange);
					date = Util.convertDateStrToDate(dateStr);
					photo.setLastChange(date);
					photo.setFlagSent(cursor.getInt(idFlagSent));
					photo.setCordinateX(cursor.getDouble(cordinateX));		
					photo.setCordinateY(cursor.getDouble(cordinateY));
					photos.add(photo);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e( Constants.CATEGORY , e.getMessage());
			throw new RepositoryException(context.getResources().getString(
					R.string.db_error_search_record));
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return photos;
	}
	
}
