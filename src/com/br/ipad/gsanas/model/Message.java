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
import java.util.Vector;

import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

import android.provider.BaseColumns;

/**
 * [GSANAS] Basic Class - Message
 * 
 * @author Thúlio Araújo
 * @since 08/09/2011
 */
public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String descriptionMessage;
	private Integer icUseSituation;
	private Integer icAlert;
	private Date lastChange;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescriptionMessage() {
		return descriptionMessage;
	}

	public void setDescriptionMessage(String descriptionMessage) {
		this.descriptionMessage = descriptionMessage;
	}

	public Integer getIcUseSituation() {
		return icUseSituation;
	}

	public void setIcUseSituation(Integer icUseSituation) {
		this.icUseSituation = icUseSituation;
	}
	
	public Date getLastChange() {
		return lastChange;
	}

	public void setLastChange(String lastChange) {
		this.lastChange =  Util.getData(lastChange);
	}

	private static String[] columns = new String[] { Messages.ID, Messages.DESCRIPTIONMESSAGE, Messages.ICUSESITUATION, Messages.LASTCHANGE, Messages.ICALERT};
	
	public static String[] getColumns(){
		return columns;
	}	
	
	public static final class Messages implements BaseColumns {
		public static final String ID = "MESG_ID";
		public static final String DESCRIPTIONMESSAGE = "MESG_DSMESSAGE";
		public static final String ICUSESITUATION = "MESG_ICUSESITUATION";
		public static final String LASTCHANGE = "MESG_TMLASTCHANGE";
		public static final String ICALERT = "MESG_ICALERT";
	}
	
	public static Message insertFromFile(Vector<String> obj){
		
		Message setedObject = new Message();
		setedObject.setDescriptionMessage(obj.get(1));		
		setedObject.setIcUseSituation(Constants.NO);
		String date = Util.convertDateToDateStr(Util.getCurrentDateTime());
		setedObject.setIcAlert((Constants.NO));
		setedObject.setLastChange(date);
		
		return setedObject;
	
	}

	public Integer getIcAlert() {
		return icAlert;
	}

	public void setIcAlert(Integer icAlert) {
		this.icAlert = icAlert;
	}
}