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

package com.br.ipad.gsanas.model;

import java.io.Serializable;
import java.util.Date;

import android.provider.BaseColumns;

/**
 * [GSANAS] Basic Class - Photo
 * 
 * @author Thúlio Araújo
 * @since 05/07/2011
 */
public class Photo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private ServiceOrder serviceOrder;
	private PhotoType photoType;
	private String descriptionObservation;
	private String path;
	private Integer flagSent;
	private Double cordinateX;
	private Double cordinateY;
	private Date lastChange;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ServiceOrder getServiceOrder() {
		return serviceOrder;
	}

	public void setServiceOrder(ServiceOrder serviceOrder) {
		this.serviceOrder = serviceOrder;
	}

	public PhotoType getPhotoType() {
		return photoType;
	}

	public void setPhotoType(PhotoType photoType) {
		this.photoType = photoType;
	}

	public String getDescriptionObservation() {
		return descriptionObservation;
	}

	public void setDescriptionObservation(String descriptionObservation) {
		this.descriptionObservation = descriptionObservation;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getFlagSent() {
		return flagSent;
	}

	public void setFlagSent(Integer flagSent) {
		this.flagSent = flagSent;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public void setLastChange(Date lastChange) {
		this.lastChange = lastChange;
	}

	public Double getCordinateX() {
		return cordinateX;
	}

	public void setCordinateX(Double cordinateX) {
		this.cordinateX = cordinateX;
	}

	public Double getCordinateY() {
		return cordinateY;
	}

	public void setCordinateY(Double cordinateY) {
		this.cordinateY = cordinateY; 
	}

	private static String[] columns = new String[] { Photos.ID, Photos.SERVICEORDER, Photos.PHOTOTYPE, Photos.DESCRIPTIONOBSERVATION,
		Photos.PATH, Photos.LASTCHANGE,Photos.CORDINATEX,Photos.CORDINATEY, Photos.FLAGSENT };

	public static String[] getColumns(){
		return columns;
	}	

	public static final class Photos implements BaseColumns {
		public static final String ID = "PHOT_ID";
		public static final String SERVICEORDER = "SEOR_ID";
		public static final String PHOTOTYPE = "PHTT_ID";
		public static final String DESCRIPTIONOBSERVATION = "PHOT_DSOBSERVATION";
		public static final String PATH = "PHOT_PATH";
		public static final String CORDINATEX = "PHOT_CORDINATEX";
		public static final String CORDINATEY = "PHOT_CORDINATEY";
		public static final String LASTCHANGE = "PHOT_TMLASTCHANGE";
		public static final String FLAGSENT = "PHOT_FLAGSENT";
	}
}