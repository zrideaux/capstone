import {
  fetchBlobstoreUrlAndSendData,
  sendFormData,
} from './../blobstore.js';

/**
 * Creates a listing preview for the user to see.
 */
function displayPreviewListing(queryString, sendFormData) {
  const containerElement = document.getElementById("preview");
  containerElement.innerHTML = '';

  fetchBlobstoreUrlAndSendData(queryString, sendFormData);
}

/**
 * Send new-listing form and input related data to the imageUploadURL and 
 *     create a preview with the response from the imageUploadURL servlet.
 * 
 * @param imageUploadURL the url to send listing data to.
 */
function createPreviewSendFormDataFunc(appendDataFunc, createPreviewListingFunc) {
  return (imageUploadURL) => {
    const newListingForm = document.getElementById('new-listing-form');
    sendFormData(appendDataFunc, newListingForm, imageUploadURL, 
      createPreviewListingFunc);
  }
}

/**
 * Creates a listing given a Listing JSON and appends the listing the the 
 *     preview container element in the newlisting page.
 *
 * @param listing JSON that represents a Listing object.
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
}
