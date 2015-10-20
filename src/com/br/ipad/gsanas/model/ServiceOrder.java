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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import android.content.Context;
import android.provider.BaseColumns;

import com.br.ipad.gsanas.connetion.WebServerConnection;
import com.br.ipad.gsanas.controller.ServiceOrderController;
import com.br.ipad.gsanas.exception.ControllerException;
import com.br.ipad.gsanas.model.LigacaoAguaMaterial.LigacaoAguaMateriais;
import com.br.ipad.gsanas.util.Util;

/**
 * [GSANAS] Basic Class - Service Order
 * 
 * @author Thúlio Araújo
 * @since 05/07/2011
 */
public class ServiceOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Date programmingDate;
	private SpecialEquipments specialEquipments;
	private ServiceOrderSituation serviceOrderSituation;
	private OSProgramNotClosedReason osProgramNotClosedReason;
	private ServiceType serviceType;
	private Integer propertyId;
	private String propertyName;
	private Integer sequentialProgramming;
	private String referencePointDescription;
	private Integer propertyNumber;
	private String applicantName;
	private String phoneNumber;
	private String descriptionAddres;
	private String waterConnectionStatusDescription;
	private String sewerConnectionStatusDescription;
	private String propertyRegistration;
	private String hydrometerNumber;
	private String hydrometerCapacityDescription;
	private Date lastChange;
	private Integer flagSent;
	private Integer flagChangedSituation;
	private Integer excluded;
	private String observation;
	
	private LigacaoAguaMaterial ligacaoAguaMaterial;
	private BigDecimal profundidade;
	private BigDecimal diametro;
	private BigDecimal distanciaRedeLinhaAgua;
	private String descricaoElementoReferencia;
	private String codigoOSTerceiro;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIdString(String id) {
		this.id = Util.verificarNuloInt(id);
	}

	public Date getProgrammingDate() {
		return programmingDate;
	}

	public void setProgrammingDate(String programmingDate) {
		this.programmingDate = Util.convertDateStrToDate1(programmingDate);
	}

	public SpecialEquipments getSpecialEquipments() {
		return specialEquipments;
	}

	public void setSpecialEquipments(SpecialEquipments specialEquipments) {
		this.specialEquipments = specialEquipments;
	}

	public ServiceOrderSituation getServiceOrderSituation() {
		return serviceOrderSituation;
	}

	public void setServiceOrderSituation(ServiceOrderSituation serviceOrderSituation, Context context) {
		this.serviceOrderSituation = serviceOrderSituation;
		
		//Envia apenas se a data de programação for igual a atual ou é a primeira vez que está inserindo uma OS
		Date programmingDate;
		try {
			programmingDate = ServiceOrderController.getInstance().getFirstServiceOrderDate();
			
			if ( context != null && programmingDate != null &&
					Util.isEqualsDates(programmingDate, Util.getCurrentDateTime()) || programmingDate == null && context != null){
				
				// Alteramosos status no GSAN
				class ChangeSituation extends Thread{
					
					public Context context = null;
					public ServiceOrderSituation serviceOrderSituation = null;
					public Integer idOS = null;
					
					public void run(){
				    	WebServerConnection web =  WebServerConnection.getInstance();
				    	web.setContext(context);
						web.changeServiceOrderSituation(idOS, serviceOrderSituation.getId().shortValue() );
					}
					
				}
				
				ChangeSituation change = new ChangeSituation();
				change.context = context;
				change.idOS = this.getId();
				change.serviceOrderSituation = serviceOrderSituation;
				
				change.start();
			}
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OSProgramNotClosedReason getOsProgramNotClosedReason() {
		return osProgramNotClosedReason;
	}

	public void setOsProgramNotClosedReason(
			OSProgramNotClosedReason osProgramNotClosedReason) {
		this.osProgramNotClosedReason = osProgramNotClosedReason;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = Util.verificarNuloInt(propertyId);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Integer getSequentialProgramming() {
		return sequentialProgramming;
	}

	public void setSequentialProgramming(String sequentialProgramming) {
		this.sequentialProgramming = Util.verificarNuloInt(sequentialProgramming);
	}

	public String getReferencePointDescription() {
		return referencePointDescription;
	}

	public void setReferencePointDescription(String referencePointDescription) {
		this.referencePointDescription = referencePointDescription;
	}

	public Integer getPropertyNumber() {
		return propertyNumber;
	}

	public void setPropertyNumber(String propertyNumber) {
		this.propertyNumber = Util.verificarNuloInt(propertyNumber);
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setFlagChangedSituation(Integer flagChangedSituation) {
		this.flagChangedSituation = flagChangedSituation;
	}

	public Integer getFlagChangedSituation() {
		return flagChangedSituation;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDescriptionAddres() {
		return descriptionAddres;
	}

	public void setDescriptionAddres(String descriptionAddres) {
		this.descriptionAddres = descriptionAddres;
	}

	public String getWaterConnectionStatusDescription() {
		return waterConnectionStatusDescription;
	}

	public void setWaterConnectionStatusDescription(
			String waterConnectionStatusDescription) {
		this.waterConnectionStatusDescription = waterConnectionStatusDescription;
	}

	public String getSewerConnectionStatusDescription() {
		return sewerConnectionStatusDescription;
	}

	public void setSewerConnectionStatusDescription(
			String sewerConnectionStatusDescription) {
		this.sewerConnectionStatusDescription = sewerConnectionStatusDescription;
	}

	public String getPropertyRegistration() {
		return propertyRegistration;
	}

	public void setPropertyRegistration(String propertyRegistration) {
		this.propertyRegistration =  propertyRegistration;
	}

	public String getHydrometerNumber() {
		return hydrometerNumber;
	}

	public void setHydrometerNumber(String hydrometerNumber) {
		this.hydrometerNumber = hydrometerNumber;
	}

	public String getHydrometerCapacityDescription() {
		return hydrometerCapacityDescription;
	}

	public void setHydrometerCapacityDescription(
			String hydrometerCapacityDescription) {
		this.hydrometerCapacityDescription = hydrometerCapacityDescription;
	}

	public Date getLastChange() {
		return lastChange;
	}

	public void setLastChange(String lastChange) {
		this.lastChange = Util.getData(lastChange);
	}

	public Integer getFlagSent() {
		return flagSent;
	}

	public void setFlagSent(Integer flagSent) {
		this.flagSent = flagSent;
	}

	public Integer getExcluded() {
		return excluded;
	}

	public void setExcluded(String excluded) {
		this.excluded = Util.verificarNuloInt(excluded);
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	

	public LigacaoAguaMaterial getLigacaoAguaMaterial() {
		return ligacaoAguaMaterial;
	}

	public void setLigacaoAguaMaterial(LigacaoAguaMaterial ligacaoAguaMaterial) {
		this.ligacaoAguaMaterial = ligacaoAguaMaterial;
	}

	public BigDecimal getProfundidade() {
		return profundidade;
	}

	public void setProfundidade(BigDecimal profundidade) {
		this.profundidade = profundidade;
	}

	public BigDecimal getDiametro() {
		return diametro;
	}

	public void setDiametro(BigDecimal diametro) {
		this.diametro = diametro;
	}

	public BigDecimal getDistanciaRedeLinhaAgua() {
		return distanciaRedeLinhaAgua;
	}

	public void setDistanciaRedeLinhaAgua(BigDecimal distanciaRedeLinhaAgua) {
		this.distanciaRedeLinhaAgua = distanciaRedeLinhaAgua;
	}

	public String getDescricaoElementoReferencia() {
		return descricaoElementoReferencia;
	}

	public void setDescricaoElementoReferencia(String descricaoElementoReferencia) {
		this.descricaoElementoReferencia = descricaoElementoReferencia;
	}



	public String getCodigoOSTerceiro() {
		return codigoOSTerceiro;
	}

	public void setCodigoOSTerceiro(String codigoOSTerceiro) {
		this.codigoOSTerceiro = codigoOSTerceiro;
	}



	private static String[] columns = new String[] { ServiceOrders.ID,ServiceOrders.PROGRAMMINGDATE,ServiceOrders.SPECIALEQUIPMENTS, ServiceOrders.SERVICEORDERSITUATION, ServiceOrders.OSPROGRAMNOTCLOSEDREASON,
		ServiceOrders.SERVICETYPE, ServiceOrders.PROPERTYID, ServiceOrders.SEQUENTIALPROGRAMMING, ServiceOrders.REFERENCEPOINTDESCRIPTION,
		ServiceOrders.PROPERTYNUMBER, ServiceOrders.APPLICANTNAME, ServiceOrders.PHONENUMBER, ServiceOrders.DESCRIPTIONADDRES,
		ServiceOrders.WATERCONNECTIONSTATUSDESCRIPTION, ServiceOrders.SEWERCONNECTIONSTATUSDESCRIPTION, ServiceOrders.PROPERTYREGISTRATION,
		ServiceOrders.HYDROMETERNUMBER, ServiceOrders.HYDROMETERCAPACITYDESCRIPTION, ServiceOrders.LASTCHANGE, ServiceOrders.EXCLUDED, ServiceOrders.FLAG_SENT,ServiceOrders.FLAG_CHANGED_SITUATION,ServiceOrders.OBSERVATIONDESCRIPTION,
		ServiceOrders.LIGACAOAGUAMATERIAL, ServiceOrders.PROFUNDIDADE, ServiceOrders.DIAMETRO, ServiceOrders.DISTANCIAREDELIGACAOAGUA, ServiceOrders.DESCRICAOELEMENTOREFERENCIA, ServiceOrders.CODIGOOSTERCEIRO};

	public static String[] getColumns(){
		return columns;
	}	

	public static final class ServiceOrders implements BaseColumns {
		public static final String ID = "SEOR_ID";
		public static final String PROGRAMMINGDATE = "SEOR_PROGRAMMINGDATE";
		public static final String SPECIALEQUIPMENTS = "SPEQ_ID";
		public static final String SERVICEORDERSITUATION = "SOST_ID";
		public static final String OSPROGRAMNOTCLOSEDREASON = "OPNR_ID";
		public static final String SERVICETYPE = "SVTP_ID";
		public static final String PROPERTYID = "PRPT_PROPERTYID";
		public static final String SEQUENTIALPROGRAMMING = "SEOR_NNSEQUENTIALPROGRAMMING";
		public static final String REFERENCEPOINTDESCRIPTION = "SEOR_DSREFPOINTDESCRIPTION";
		public static final String PROPERTYNUMBER = "SEOR_NNPROPERTY";
		public static final String APPLICANTNAME = "SEOR_NMAPPLICANTNAME";
		public static final String PHONENUMBER = "SEOR_NNPHONE";
		public static final String DESCRIPTIONADDRES = "SEOR_DSADDRES";
		public static final String WATERCONNECTIONSTATUSDESCRIPTION = "LAST_DSWATERCONNECTIONSTATUS";
		public static final String SEWERCONNECTIONSTATUSDESCRIPTION = "LEST_DSSEWERCONNECTIONSTATUS";
		public static final String PROPERTYREGISTRATION = "PRPT_PROPERTYREGISTRATION";
		public static final String HYDROMETERNUMBER = "HIDR_NNHYDROMETER";
		public static final String HYDROMETERCAPACITYDESCRIPTION = "HICP_DSHYDROMETERCAPACITY";
		public static final String LASTCHANGE = "SEOR_TMLASTCHANGE";
		public static final String EXCLUDED = "SEOR_ICEXCLUDED";
		public static final String FLAG_SENT = "SEOR_FLAGSENT";
		public static final String FLAG_CHANGED_SITUATION = "SEOR_FLAGCHANGEDSIT";
		public static final String OBSERVATIONDESCRIPTION = "SEOR_DSOBSERVATION";
		public static final String LIGACAOAGUAMATERIAL = LigacaoAguaMateriais.ID;
		public static final String PROFUNDIDADE = "OSAS_NNPROFUNDIDADE";
		public static final String DIAMETRO = "OSAS_NNDIAMETRO";
		public static final String DISTANCIAREDELIGACAOAGUA = "OSAS_NNDISTANCIAREDE";
		public static final String DESCRICAOELEMENTOREFERENCIA = "OSAS_DSELEMENTREFERENCIA";
		public static final String CODIGOOSTERCEIRO = "SEOR_CODTERCEIRO";
	}
	
	public static ServiceOrder insertFromFile(Vector<String> obj){
		ServiceOrder setedObject = new ServiceOrder();		
		setedObject.setIdString(obj.get(1));
		setedObject.setProgrammingDate(obj.get(2));
		setedObject.setSequentialProgramming(obj.get(3));
		
		ServiceOrderSituation soSituation = new ServiceOrderSituation();
		soSituation.setIdString(obj.get(4));
		setedObject.setServiceOrderSituation(soSituation, null);

		setedObject.setPropertyId(obj.get(5));
		setedObject.setReferencePointDescription(obj.get(6));
		setedObject.setPropertyName(obj.get(7));
		setedObject.setApplicantName(obj.get(8));	
		setedObject.setPhoneNumber(obj.get(9));
		setedObject.setDescriptionAddres(obj.get(10));
		setedObject.setWaterConnectionStatusDescription(obj.get(11));
		setedObject.setSewerConnectionStatusDescription(obj.get(12));
		setedObject.setPropertyRegistration(obj.get(13));	
		setedObject.setHydrometerNumber(obj.get(14));	
		setedObject.setHydrometerCapacityDescription(obj.get(15));	
		setedObject.setObservation(obj.get(18));	
		
		ServiceType servType = new ServiceType();
		servType.setIdString(obj.get(16));
		setedObject.setServiceType(servType);
		
		setedObject.setExcluded(obj.get(17));
		String date = Util.convertDateToDateStr(Util.getCurrentDateTime());
		setedObject.setLastChange(date);
		
		//seta os objetos nulos
		SpecialEquipments specialEqp = new SpecialEquipments();
		specialEqp.setIdString(null);
		setedObject.setSpecialEquipments(specialEqp);
		
		//seta os objetos nulos
		OSProgramNotClosedReason reason = new OSProgramNotClosedReason();
		reason.setIdString(null);
		setedObject.setOsProgramNotClosedReason(reason);
		
		setedObject.setCodigoOSTerceiro(obj.get(23));
		
		return setedObject;
	
	}
}