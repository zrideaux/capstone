import {
  fetchBlobstoreUrlAndSendData,
  sendFormData,
} from './../blobstore.js';

/**
 * Creates a listing preview for the user to see.
 *
 * @param queryString a string that represents a query.
 * @param sendFormData the function that calls sendFormData with the 
 *     appropriate parameters. The function has an imageUploadUrl parameter.
 */
function displayPreviewListing(queryString, sendFormData) {
  const containerElement = document.getElementById("preview");
  containerElement.innerHTML = '';

  fetchBlobstoreUrlAndSendData(queryString, sendFormData);
}

/**
 * Creates a function that sends the new-listing form and input related data to 
 *     the imageUploadURL and creates a preview with the response from the 
 *     imageUploadURL servlet.
 * 
 * @param appendDataFunc a function that appends data to a Form Data and has a 
 *     FormData parameter.
 * @param createPreviewListingFunc a function that creates a listing preview.
 * @return a create preview send form data function.
 */
function createPreviewSendFormDataFunc(appendDataFunc, createPreviewListingFunc) {
  return (imageUploadURL) => {
    const newListingForm = document.getElementById('new-listing-form');
    sendFormData(appendDataFunc, newListingForm, imageUploadURL, 
      createPreviewListingFunc);
  }
}

/**
 * Creates a function that creates a listing preview and appends it to the  
 *     listing preview container element in the newlisting page.
 *
 * @param previewListingFunc a function that creates the preview listing.
 * @return a create preview listing func.
 */
function createCreatePreviewListingFunc(previewListingFunc) {
  return (response) => {
    const containerElement = document.getElementById("preview");
    
    previewListingFunc(containerElement, response);

    $(document).ready(function(){
      const $preview = $('#preview');
      const bottom = $preview.position().top + $preview.offset().top + 
          $preview.outerHeight(true);
      $('html, body').animate({
          scrollTop: bottom
      }, 900);
    });
  }
}

export {
  createCreatePreviewListingFunc,
  createPreviewSendFormDataFunc,
  displayPreviewListing
};
