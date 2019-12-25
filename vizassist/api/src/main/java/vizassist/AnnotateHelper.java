// Imports the Google Cloud client library
package vizassist;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.protobuf.ByteString;

public class AnnotateHelper {
	public static JSONObject Annotate(Image img) throws Exception {
		JSONObject annotationObj = new JSONObject();

		// Build annotate request
		List<AnnotateImageRequest> requests = new ArrayList<>();
		Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		// Not required when running on GCE because the default service account to run
		// GCE VM can do authentication directly.
		Credentials myCredentials = ServiceAccountCredentials
				.fromStream(new FileInputStream("C:\\Users\\sean_\\Downloads\\credentials.json"));
		ImageAnnotatorSettings imageAnnotatorSettings = ImageAnnotatorSettings.newBuilder()
				.setCredentialsProvider(FixedCredentialsProvider.create(myCredentials)).build();

		// Initiate a client
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create(imageAnnotatorSettings)) {
			AnnotateImageResponse res = client.batchAnnotateImages(requests).getResponsesList().get(0);
			client.close();

			if (res.hasError()) {
				System.out.printf("Error: %s\n", res.getError().getMessage());
				return new JSONObject();
			}

			// For full list of available annotations, see http://g.co/cloud/vision/docs
			TextAnnotation annotation = res.getFullTextAnnotation();
			annotationObj.put("text", annotation.getText());
		}
		return annotationObj;
	}

	public static void main(String... args) throws Exception {

		ByteString imgBytes = ByteString.readFrom(new FileInputStream("C:\\Users\\sean_\\Downloads\\hour.jpg"));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		JSONObject annotation = Annotate(img);
		System.out.println(annotation.toString(2));
	}
}