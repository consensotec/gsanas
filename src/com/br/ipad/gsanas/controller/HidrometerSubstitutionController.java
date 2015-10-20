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

import com.br.ipad.gsanas.exception.BusinessException;
import com.br.ipad.gsanas.exception.ControllerException;
import com.br.ipad.gsanas.exception.RepositoryException;
import com.br.ipad.gsanas.model.HidrometerSubstitution;
import com.br.ipad.gsanas.repository.HidrometerSubstitutionRepository;
import com.br.ipad.gsanas.repository.IHidrometerSubstitutionRepository;
import com.br.ipad.gsanas.ui.R;

/**
 * [GSANAS] Controller - HidrometerSubstitution
 * 
 * @author Fernanda Almeida
 * @since 13/04/2012
 */
public class HidrometerSubstitutionController extends BasicController implements IHidrometerSubstitutionController {
	
	private IHidrometerSubstitutionRepository hidrometerSubstitutionRepository;
	
	private static HidrometerSubstitutionController instance;	
	
	public void resetInstance(){
		instance = null;
	}
	
	private HidrometerSubstitutionController(){
		super();
		hidrometerSubstitutionRepository = ( IHidrometerSubstitutionRepository )HidrometerSubstitutionRepository.getInstance();
	}
	
	public static HidrometerSubstitutionController getInstance(){
		if ( instance == null ){
			instance =  new HidrometerSubstitutionController();
		} 
		return instance;
	}
	
	public HidrometerSubstitution getHidrometerSubstitutionById(int id) throws ControllerException {
		try{
			return hidrometerSubstitutionRepository.getHidrometerSubstitutionById( id );
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}
	}
	
	public ArrayList<HidrometerSubstitution> getAllHidrometersSubstitutions() throws ControllerException {
		try{
			return hidrometerSubstitutionRepository.getAllHidrometersSubstitutions();
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}
	}
		
	//validations
	public void validateHidrometerLocalInstalation(HidrometerSubstitution hidrometerSubstitution)
			throws BusinessException, ControllerException{		
		if(HidrometerLocalInstalationController.getInstance().getHidrometerLocalInstalationById(hidrometerSubstitution.getHidrometerLocalInstalation().getId())==null){
			throw new BusinessException(context.getResources().getString(R.string.str_business_error));
		}				
	}
	
	//validations
	public void validateProtectionHidrometer(HidrometerSubstitution hidrometerSubstitution)
			throws BusinessException, ControllerException{		
		if(ProtectionHidrometerController.getInstance().getProtectionHidrometerById(hidrometerSubstitution.getProtectionHidrometer().getId())==null){
			throw new BusinessException(context.getResources().getString(R.string.str_business_error));
		}				
	}
	
	//validations
	public void validateServiceOrder(HidrometerSubstitution hidrometerSubstitution)
			throws BusinessException, ControllerException{		
		if(ServiceOrderController.getInstance().getServiceOrderById(hidrometerSubstitution.getServiceOrder().getId())==null){
			throw new BusinessException(context.getResources().getString(R.string.str_business_error));
		}				
	}

	public void insertHidrometerSubstitution(HidrometerSubstitution hidrometerSubstitution) throws ControllerException, BusinessException {
		if(hidrometerSubstitution.getHidrometerLocalInstalation() != null){
			if(hidrometerSubstitution.getHidrometerLocalInstalation().getId() != null){
				validateHidrometerLocalInstalation(hidrometerSubstitution);
			}
		}
		if(hidrometerSubstitution.getProtectionHidrometer() != null){
			if(hidrometerSubstitution.getProtectionHidrometer().getId() != null){
				validateProtectionHidrometer(hidrometerSubstitution);
			}
		}
		
		if(hidrometerSubstitution.getServiceOrder() != null){
			if(hidrometerSubstitution.getServiceOrder().getId() != null){
				validateProtectionHidrometer(hidrometerSubstitution);
			}
		}
			
		try{
			hidrometerSubstitutionRepository.insertHidrometerSubstitution(hidrometerSubstitution);
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}
	}

	public void updateHidrometerSubstitution(HidrometerSubstitution hidrometerSubstitution) throws ControllerException, BusinessException {
		if(hidrometerSubstitution.getHidrometerLocalInstalation() != null){
			if(hidrometerSubstitution.getHidrometerLocalInstalation().getId() != null){
				validateHidrometerLocalInstalation(hidrometerSubstitution);
			}
		}
		if(hidrometerSubstitution.getProtectionHidrometer() != null){
			if(hidrometerSubstitution.getProtectionHidrometer().getId() != null){
				validateProtectionHidrometer(hidrometerSubstitution);
			}
		}
		
		try{
			hidrometerSubstitutionRepository.updateHidrometerSubstitution(hidrometerSubstitution);
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}			
	}

	public void removeHidrometerSubstitution(HidrometerSubstitution hidrometerSubstitution) throws ControllerException {
		try{
			hidrometerSubstitutionRepository.removeHidrometerSubstitution(hidrometerSubstitution);
		} catch ( RepositoryException e ){
			throw new ControllerException( e.getMessage() );
		}			
	}
	
}
