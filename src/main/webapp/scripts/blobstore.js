import { 
  isErrorMessage,
  displayErrorMessage
} from './miscellaneous.js';

/**
 * Fetches a blobstore url from the blobstore servlet and then calls a 
 *     sendFormDataFunc to send FormData to a servlet
 * 
 * @param servletUrl the servlet that the blobstore url will POST data to
 * @param sendFormDataFunc the function that calls sendFormData with the 
 *     appropriate parameters. The function has an imageUploadUrl parameter.
 */
function fetchBlobstoreUrlAndSendData(servletUrl, sendFormDataFunc) {
  const queryString = "/blobstore-upload-url?servlet-url=" + servletUrl;
  fetch(queryString)
      .then(response => response.json())
      .then((imageUploadUrl) => {
        if(isErrorMessage(imageUploadUrl)) {
          displayErrorMessage(imageUploadUrl);
        } else {
          console.log("Send form data.");
          sendFormDataFunc(imageUploadUrl);
        }
      })
}

/** 
 * Sends converts a form and its input values into a FormData object. More data 
 *     can be added to the FormData with a appendData func. The FromData will 
 *     be passed into an XMLHttpRequest to an uploadURL. Once the response is 
 *     ready and the status is ok an onloadFunc will be called which is passed 
 *     a JSON.
 * 
 * @param appendData a function that appends data to a Form Data and has a 
 *     FormData parameter.
 * @param form a form that will be turned into a FormData object 
 * @param uploadURL the url that the XMLHttpRequest will send the FormData to
 * @param onloadFunc the function that will execute when once the response is 
 *     ready and the status is ok. This function has a JSON parameter.
 */
function sendFormData(appendData, form, uploadURL, onloadFunc) {
  const data = new FormData(form);

  // Add parameters to the formData
  appendData(data);

  // prints the input name and value
  for (var key of data.entries()) {
    console.log(key[0] + ', ' + key[1]);
  }

  const req = new XMLHttpRequest();
  req.open("POST", uploadURL, true);
  req.onload = function() {
    // when response is ready and status is ok
    if (req.status == 200) {
      console.log("Response text: " + req.responseText);
      const response = JSON.parse(req.responseText);
      if (isErrorMessage(response)) {
        displayErrorMessage(response);
      } else {
        console.log("Uploaded!");
        onloadFunc(response);
      }
    } else {
      const errorMessage = "Error " + req.status + 
          " occurred when trying to upload your form.";
      displayErrorMessage(errorMessage);
    }
  };
  req.onerror = function() {
    const errorMessage = "An error occurred during the transaction,";
    displayErrorMessage(errorMessage);
  };
  req.send(data);
}

export {
  fetchBlobstoreUrlAndSendData,
  sendFormData
};
