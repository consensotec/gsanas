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

package com.br.ipad.gsanas.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.br.ipad.gsanas.connetion.PhotoConnection;
import com.br.ipad.gsanas.exception.BusinessException;
import com.br.ipad.gsanas.exception.FacadeException;
import com.br.ipad.gsanas.facade.Facade;
import com.br.ipad.gsanas.model.Photo;
import com.br.ipad.gsanas.model.PhotoType;
import com.br.ipad.gsanas.model.ServiceOrder;
import com.br.ipad.gsanas.model.ServiceOrderSituation;
import com.br.ipad.gsanas.util.Constants;
import com.br.ipad.gsanas.util.Util;

public class Gallery extends Activity implements LocationListener {
			
	private Vector<ImageView> mySDCardImages;
	
	private int photoType;
	private Button btMenu;
	private int count;
	//array que guarda o caminho completo para cada foto
	private String[] arrPath;
	
	private ImageAdapter imageAdapter;

	private Double latitude = 0.0;
	private Double longitude = 0.0;
	
	//ids dos thumbs
	private Integer[] mThumbIds;
	private String directory;
	ImageView myImageView;
	List<Integer> drawablesId ;
	
	private ServiceOrder serviceOrder;
	private Integer idServOrder;
	
	protected LocationManager locationManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Facade.setContext(this);
		setContentView(R.layout.gallery);
		mySDCardImages = new Vector<ImageView>();
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, Constants.GPS_PERIOD, 500, this);
		
		serviceOrder = (ServiceOrder) getIntent().getSerializableExtra("serviceOrder");
		idServOrder = serviceOrder.getId();
		
		GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
		btMenu = (Button) findViewById(R.id.btMenu);
		
		photoType = getIntent().getIntExtra("photoType",0);
				
		if(photoType==Constants.BEGINNING){
			directory = Constants.BEGINNING_DIRECTORY;
		}
		if(photoType==Constants.DURING){
			directory = Constants.DURING_DIRECTORY;
		}
		if(photoType==Constants.END){
			directory = Constants.END_DIRECTORY;
		}
		if(photoType==Constants.HIDROMETER){
			directory = Constants.HIDROMETER_DIRECTORY;
		}
			
		
		imagegrid.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Intent intent = new Intent(Gallery.this,GalleryItem.class);
	        	intent.putExtra("photoPath", arrPath[position]);
	        	intent.putExtra("photoType", photoType);
	        	intent.putExtra("serviceOrder", serviceOrder);
	        	startActivity(intent);
	        }
	    });		
		
		btMenu = (Button) findViewById(R.id.btMenu);
		btMenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				openOptionsMenu();
			}
		});
		
	}		

	
	@Override
	protected void onResume() {
		
		super.onResume();
		//id da imagem
		int picIndex=12345;  
		
		File sdDir = new File("/sdcard/gsanas/"+idServOrder+"/"+directory);
		File[] sdDirFiles = sdDir.listFiles();
			
	
		// Exibe os thumbs das fotos
		arrPath = new String[sdDirFiles.length];
		count = 0;
		//remove os elementos do vetor para nao acumular
		mySDCardImages.removeAllElements();
		drawablesId = null;
		drawablesId = new ArrayList<Integer>();
		
		for(File singleFile : sdDirFiles)
		{
			arrPath[count] = singleFile.getAbsolutePath();
			Bitmap bt = Util.decodeFile(singleFile);
			myImageView = new ImageView(Gallery.this.getApplicationContext());
			//uso de menos memoria
		    myImageView.setDrawingCacheEnabled(false);
		    
		    myImageView.setImageBitmap(bt);
		    
		    myImageView.setId(picIndex);
		    picIndex++;
		    
		    drawablesId.add(myImageView.getId());
		    mySDCardImages.add(myImageView);
		    myImageView = null;
		    count++;
		} 
				
		mThumbIds = (Integer[])drawablesId.toArray(new Integer[0]);

		GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
		imageAdapter = new ImageAdapter(Gallery.this);
		imagegrid.setAdapter(imageAdapter);
	
				
		
	}
	
	//elimina todos os callbacks feitos ao gridview = melhora o desempenho, libera memoria
	@Override
	protected void onPause() {
	    GridView gridView = (GridView) findViewById(R.id.PhoneImageGrid);
	    int count = gridView.getCount();
	    for (int i = 0; i < count; i++) {
	        ImageView v = (ImageView) gridView.getChildAt(i);
	        if (v != null) {
	            if (v.getDrawable() != null) v.getDrawable().setCallback(null);
	        }
	    }
	    super.onPause();
	}	
	 
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) { 
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }
   
	        imageView.setImageDrawable(mySDCardImages.get(position).getDrawable());

	        return imageView;
	    }
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0,Constants.TAKEPICTURE,0,R.string.str_gallery_take_picture);
    	return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	
    	if(photoType == Constants.HIDROMETER){
    		AlertDialog.Builder alert = new AlertDialog.Builder(Gallery.this);
			alert.setMessage(getString(R.string.str_gallery_hidrometer_error));
			alert.setNeutralButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					
				}
			});
			alert.show();
			return false;
    	}
    	switch (item.getItemId()){
    	case Constants.TAKEPICTURE:
    		
    		if(count < 3){
    			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, Constants.CAMERA_TAKE_PICTURE);
				return true;
    		}
    		else{
    			AlertDialog.Builder alert = new AlertDialog.Builder(Gallery.this);
    			alert.setMessage(getString(R.string.str_gallery_3_photos_error));
    			alert.setNeutralButton(Constants.ALERT_OK, new DialogInterface.OnClickListener() {		
    				public void onClick(DialogInterface arg0, int arg1) {
    				}
    			});
    			alert.show();
    		}
    	}
    	return super.onMenuItemSelected(featureId, item); 
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//verifica se é o código de foto
		if(requestCode == Constants.CAMERA_TAKE_PICTURE) {
		//verifica se a foto foi salva			
			if (resultCode == RESULT_OK) {
				
				// Verifica se a falta está pausada e a inicia
				if(serviceOrder.getServiceOrderSituation().getId() == ServiceOrderSituation.SITUATION_PAUSED){
					
					try {
						Facade.getInstance().updatePausedSituation(serviceOrder, Constants.UNIQUE_ACTIVITY,Gallery.this);
					} catch (FacadeException e) {
						Log.e( Constants.CATEGORY , e.getMessage());
						e.printStackTrace();
					}											
					
				}

	    		String date = Util.convertDateToDateStrFile();
				String fileName = photoType+"_"+date+".jpg";
				
				//pega a foto
				Bundle bundle = data.getExtras();
				
				String imageFilePath = "/sdcard/gsanas/"+idServOrder+"/"+directory+fileName;  
				File imageFile = new File(imageFilePath); 
				try {
					FileOutputStream out = new FileOutputStream(imageFile);
				    Bitmap bmp = (Bitmap) bundle.get("data");
				    
				    //transforma o arquivo na foto tirada
				    bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e( Constants.CATEGORY , e.getMessage());
				}
				
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if(location != null){
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				}
			
				Photo photo = new Photo();
				photo.setPath(imageFilePath);
				PhotoType phType = new PhotoType();
				phType.setId(photoType);
				photo.setPhotoType(phType);
				photo.setServiceOrder(serviceOrder);
				photo.setCordinateX(latitude);
				photo.setCordinateY(longitude);
				
				try {
					Facade.getInstance().insertPhoto(photo);
					photo = Facade.getInstance().getPhotoByPath( serviceOrder.getId(), imageFilePath );
				} catch (FacadeException e) {		
					e.printStackTrace();
					Log.e( Constants.CATEGORY , e.getMessage());
				} catch (BusinessException e) {					
					e.printStackTrace();
					Log.e( Constants.CATEGORY , e.getMessage());
				}
				
				count++;
				
				if(count < 3){
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					startActivityForResult(intent, Constants.CAMERA_TAKE_PICTURE);
				}
				
//				PhotoConnection connection = 
//						new PhotoConnection();
				
//				connection.execute(
//						imageFile,
//						serviceOrder.getId(),
//						Util.getIMEI( Gallery.this ),
//						photoType,
//						photo,
//						Gallery.this
//						);
				
//				connection.execute(serviceOrder, this);

			}
		}				
	}
    
    @Override
    public void onBackPressed() {
    	Intent i =  new Intent(Gallery.this, PhotoTypeActivity.class);
		i.putExtra("serviceOrder", serviceOrder);
    	startActivity(i);
    	
    }

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	public void onLocationChanged(Location location) {
		if(location != null){
			
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			
			//verifyGPS(provider, false);
		}
	}
}
