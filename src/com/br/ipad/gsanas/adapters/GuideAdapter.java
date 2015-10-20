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

package com.br.ipad.gsanas.adapters;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.ipad.gsanas.model.ServiceOrder;
import com.br.ipad.gsanas.ui.R;

public class GuideAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private ArrayList<ServiceOrder> serviceOrders;
	private Context c;
	private Integer idServOrder;
	
	public GuideAdapter(Context context, ArrayList<ServiceOrder> serviceOrders){
		c = context;
		this.serviceOrders = serviceOrders;
		inflater = (LayoutInflater) c
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void createDirectories(){
		//cria diretorio da OS selecionada
		File filesOs = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()+"/gsanas/"+idServOrder);  
		if(!filesOs.exists()){
			filesOs.mkdirs();
		}
		//cria diretorio de acordo com a opcao anterior selecionada
		File fileBegin = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()+"/gsanas/"+idServOrder+"/beginning");  
		if(!fileBegin.exists()){
			fileBegin.mkdirs();
		}
		File filesDuring = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()+"/gsanas/"+idServOrder+"/during");  
		if(!filesDuring.exists()){
			filesDuring.mkdirs();
		}
		File fileEnd = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath()+"/gsanas/"+idServOrder+"/end");  
		if(!fileEnd.exists()){
			fileEnd.mkdirs();
		}
		File fileHidrometer = new File(Environment.getExternalStorageDirectory()
			.getAbsolutePath()+"/gsanas/"+idServOrder+"/hidrometer");  
		if(!fileHidrometer.exists()){
			fileHidrometer.mkdirs();
		}
	}

	public int getCount() {
		
		return serviceOrders.size();
	}

	public Object getItem(int position) {
		
		return serviceOrders.get(position);
	}

	public long getItemId(int position) {
		
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {		
		if(convertView==null){
			convertView = inflater.inflate(R.layout.guide_adapter, null);
		}
		idServOrder = serviceOrders.get(position).getId();
		createDirectories();
		
		String serviceType = "";
		
		if(serviceOrders.get(position).getServiceType()!=null){
			serviceType = "<b>" + serviceOrders.get(position).getServiceType().getDescriptionServiceType() + "</b> - ";
		}
		
		TextView order = (TextView) convertView.findViewById(R.id.order);
		DecimalFormat myFormat =  new DecimalFormat("000");
		order.setText(myFormat.format(serviceOrders.get(position).getSequentialProgramming()));
		TextView address = (TextView) convertView.findViewById(R.id.address);
		address.setText(Html.fromHtml(serviceType + serviceOrders.get(position).getDescriptionAddres()));
		ImageView iv = (ImageView) convertView.findViewById(R.id.imgSituation);
		if(serviceOrders.get(position).getServiceOrderSituation()!=null){
			int osstId = serviceOrders.get(position).getServiceOrderSituation().getId();
			switch (osstId) {
			case 1:
				iv.setImageResource(R.drawable.tostart);
				break;
			case 2:
				iv.setImageResource(R.drawable.done);
				break;
			case 3:
				iv.setImageResource(R.drawable.started);
				break;
			case 5:
				iv.setImageResource(R.drawable.execution);
			default:
				break;
			}	
			if(serviceOrders.get(position).getOsProgramNotClosedReason()!=null){
				iv.setImageResource(R.drawable.pending);
			}
		}
		convertView.setTag(getItem(position));
		
		return convertView;
	}
	

}


