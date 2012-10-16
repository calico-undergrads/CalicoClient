package calico.plugins.loadImageURL.controllers;

import java.awt.image.ImageObserver;
import java.io.File;
import java.util.logging.Logger;

import calico.Calico;
import calico.CalicoDataStore;
import calico.components.CGroup;
import calico.components.CGroupImage;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.controllers.CImageController;

public class GoogleMapImageController extends CImageController {

	public static void addImage(long cuuid, long uuid, String imgPath) {
		//  CGroupImage(long uuid, long cuid, long puid, String img, int port, String localPath,
		// int imgX, int imgY, int imageWidth, int imageHeight)
		
		//CGroupImage cgImage = new CGroupImage();
//		// Get Observer
//		ImageObserver observer = getImageInitializer(cuuid, cuuid, imgPath, 300, 300);
//		
//		final long f_uuid = uuid;
//		final long f_cuid = cuuid;
//		final String f_url = imgPath;
//		// Download the Image
//		
//		Runnable download = new Runnable() {
//			@Override
//			public void run() {
//				download_image_no_exception(f_uuid, f_url);
//				return;
//				}
//		};
//		
//		Thread toRun = new Thread(download);
//		toRun.start();
//		
//		/*while(toRun.isAlive()){			
//		}*/
//		
//		System.out.println("Image Loaded....");
//		// Image is downloaded...add to canvas
//		CGroupController.create_image_group(f_uuid, f_cuid, 0L, CalicoDataStore.ServerHost, 0, getImagePath(uuid), 300, 300, 300, 300);
//	
	}
}
