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
import java.util.Vector;

import com.br.ipad.gsanas.util.Util;

import android.provider.BaseColumns;

/**
 * [GSANAS] Basic Class - Floor
 * 
 * @author Amélia Pessoa
 * @since 26/04/2012
 */
public class Floor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String description;
	private Integer indexFloorKind;
	private Integer codeFloorGSAN;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	

	public String getDescription() {
		return description;
	}

	public void setIdString(String id) {
		this.id = Util.verificarNuloInt(id);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIndexFloorKind() {
		return indexFloorKind;
	}

	public void setIndexFloorKind(Integer indexFloorKind) {
		this.indexFloorKind = indexFloorKind;
	}

	private void setIndexFloorKindString(String indexFloorKind) {
		this.indexFloorKind = Util.verificarNuloInt(indexFloorKind);
	}
	
	public Integer getCodeFloorGSAN() {
		return codeFloorGSAN;
	}

	public void setCodeFloorGSAN(Integer codeFloorGSAN) {
		this.codeFloorGSAN = codeFloorGSAN;
	}

	private void setCodeFloorGSANString(String codeFloorGSAN) {
		this.codeFloorGSAN = Util.verificarNuloInt(codeFloorGSAN);
	}
	
	private static String[] columns = new String[] { Floors.ID, Floors.DESCRIPTION, 
		Floors.INDEXFLOORKIND, Floors.CODEFLOORGSAN};

	public static String[] getColumns(){
		return columns;
	}	

	public static final class Floors implements BaseColumns {
		public static final String ID = "PAVM_ID";
		public static final String DESCRIPTION = "PAVM_DSPAVIMENTO";
		public static final String INDEXFLOORKIND = "PAVM_ICTIPOPAVIMENTO";
		public static final String CODEFLOORGSAN = "PAVM_CDPAVIMENTOGSAN";		
	}	
	
	public static Floor insertFromFile(Vector<String> obj){
		Floor setedObject = new Floor();
		
		setedObject.setDescription(obj.get(2));
		setedObject.setIndexFloorKindString(obj.get(3));
		setedObject.setCodeFloorGSANString(obj.get(1));
		
		return setedObject;	
	}
	
	public String toString(){
		return this.description;
	}
	
}