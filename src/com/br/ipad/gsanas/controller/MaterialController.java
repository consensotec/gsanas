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

package com.br.ipad.gsanas.controller;

import java.util.ArrayList;

import android.database.Cursor;

import com.br.ipad.gsanas.ui.R;
import com.br.ipad.gsanas.exception.BusinessException;
import com.br.ipad.gsanas.exception.ControllerException;
import com.br.ipad.gsanas.exception.RepositoryException;
import com.br.ipad.gsanas.model.Material;
import com.br.ipad.gsanas.repository.IMaterialRepository;
import com.br.ipad.gsanas.repository.MaterialRepository;

/**
 * [GSANAS] Controller - Material
 * 
 * @author Fernanda Almeida
 * @since 06/07/2011
 */
public class MaterialController extends BasicController implements IMaterialController {
	
	private IMaterialRepository materialRepository;
	
	private static MaterialController instance;	
	
	public void resetInstance(){
		instance = null;
	}
	
	private MaterialController(){
		super();
		materialRepository = ( IMaterialRepository )MaterialRepository.getInstance();
	}
	
	public static MaterialController getInstance(){
		if ( instance == null ){
			instance =  new MaterialController();
		} 
		return instance;
	}
	
	public Material getMaterialById(int id) throws ControllerException {
		try{
			return materialRepository.getMaterialById( id );
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}
	}
	
	public Material getMaterialByCode(String cd) throws ControllerException {
		try{
			return materialRepository.getMaterialByCode( cd );
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}
	}

	public ArrayList<Material> getAllMaterials() throws ControllerException {
		try{
			return materialRepository.getAllMaterials();
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}
	}
	
	public Cursor getAllMaterialsCursor() throws ControllerException{
		try{
			return materialRepository.getAllMaterialsCursor();
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}		
	}
	
	//validations
	public void validateMaterialUnit(Material material)
			throws BusinessException, ControllerException{		
		if(MaterialUnitController.getInstance().getMaterialUnitById(material.getMaterialUnit().getId())==null){
			throw new BusinessException(context.getResources().getString(R.string.str_business_error));
		}				
	}

	public void insertMaterial(Material material) throws ControllerException, BusinessException {
		if(material.getMaterialUnit() != null){
			if(material.getMaterialUnit().getId() != null){
				validateMaterialUnit(material);
			}
		}
			
		try{
			materialRepository.insertMaterial(material);
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}
	}

	public void updateMaterial(Material material) throws ControllerException, BusinessException {
		if(material.getMaterialUnit().getId() != null){
			if(MaterialUnitController.getInstance().getMaterialUnitById(material.getMaterialUnit().getId())==null){
				throw new BusinessException();
			}
		}
		
		try{
			materialRepository.updateMaterial(material);
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}			
	}

	public void removeMaterial(Material material) throws ControllerException {
		try{
			materialRepository.removeMaterial(material);
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}			
	}

	public String[] getAllMaterialsAdapter() throws ControllerException {
		try{
			return materialRepository.getAllMaterialsAdapter();
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}		
	}
}
