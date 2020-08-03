import { 
  createDivElement, 
  createHElement, 
  createImgElement, 
  createPElement,
  createSectionElement,
  createSpanElement
} from './htmlElement.js';

import { toggleDisplay } from './miscellaneous.js';

/**
 * Create an element that shows a listing detailed view and when clicked on will
 *     display the detailed view
 *
 * @param listing JSON that represents a listing and has listing information 
 * @param listingDisplay the display property of the detailed view
 * @param listingId the id of the listing detailed view element
 * @return a div with all the preview information pertaining to a listing
 */

export default function createListingPreview(listing, listingDisplay, listingId) {

  const sectionListing = createSectionElement('listing shadow-box', '');
  
  // Make section listing keyboard accessible 
  //     (which includes tabs and enter keys)
  sectionListing.setAttribute("tabindex", "0");

  // When the user clicks or presses enter on the section listing, change the 
  //     display of the detailed listing div from none to listingDisplay
  sectionListing.addEventListener("click", function() { 
    toggleDisplay(listingDisplay, listingId) });

  sectionListing.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
      toggleDisplay(listingDisplay, listingId);
    }
  });

  console.log("Creating listing information");
  const exImgSrc = '';
  const upvotes = listing.upvotes.toLocaleString();
  sectionListing.appendChild(createListingInformation(exImgSrc, upvotes));

  console.log('Creating listing details')
  const description = listing.description;  
  const name = listing.name;
  const type = listing.type;
  sectionListing.appendChild(createListingDetails(description, name, 
      type));

  return sectionListing;
}

/**
 * Create an element with listing information
 *
 * @param imgSrc the source of the listing image
 * @param upvotes the number of upvotes this listing has received
 * @return a div with the picture and the number of upvotes this listing has
 */
function createListingInformation(imgSrc, upvotes) {
  const divListingInfo = createDivElement('', 'preview-info', '');

  divListingInfo.appendChild(
    createImgElement(imgSrc, 'Listing preview image', 'listing-image', ''));

  divListingInfo.appendChild(
    createSpanElement('Reputation: ' + upvotes + ' upvotes', 
        'listing-reputation', ''));

  return divListingInfo;
}

/**
 * Create an element with listing details
 *
 * @param description the description associated with this listing
 * @param name the name of this listing
 * @param type the type of the listing 
 * @return a div with the description, name, and tags of a listing
 */
function createListingDetails(description, name, type) {
  const divListingDetails = createDivElement('', 'listing-info-container', '');

  console.log('Creating listing heading');
  divListingDetails.appendChild(createListingHeading(name, type));

  divListingDetails.appendChild(
    createPElement(description, '', ''));

  return divListingDetails;
}

/**
 * Create an element with listing heading
 *
 * @param name the name of this listing
 * @param type the type of the listing 
 * @return a div with the name and tags of a listing.
 */
function createListingHeading(name, type) {
  const divListingHeading = createDivElement('', 'listing-heading-container', 
      '');
  
  divListingHeading.appendChild(
    createHElement(name, 2, 'listing-preview-name', ''));

  console.log('Creating listing tags');
  divListingHeading.appendChild(createListingTags(type));

  return divListingHeading;
}

/**
 * Create an element with listing tags
 *
 * @param type the type of the listing 
 * @return a div with the type of a listing
 */
function createListingTags(type) {
  const divListingTags = createDivElement('', 'listing-tags-container', '');

  divListingTags.appendChild(createSpanElement(type, 'listing-tag', ''));

  return divListingTags;
}
