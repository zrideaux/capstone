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
 * @param listingDisplay the display property of the detailed view
 * @param listingId the id of the listing detailed view element
 * @return a div with all the preview information pertaining to a listing
 */
export default function createListingPreview(listingDisplay, listingId) {

  const sectionListing = createSectionElement('listing shadow-box', '');
  
  // Make section listing keyboard accessible 
  //     (which includes tabs and enter keys)
  sectionListing.setAttribute("tabindex", "0");

  // When the user clicks or presses enter on the section listing, change the 
  //     display of the detailed listing div from none to listingDisplay
  sectionListing.addEventListener("click", function(){ toggleDisplay(
      listingDisplay, listingId) });

  sectionListing.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
      toggleDisplay(listingDisplay, listingId);
    }
  });

  console.log("Creating listing information");
  const exImgSrc = '';
  const exUpvotes = 205;
  sectionListing.appendChild(createListingInformation(exImgSrc, exUpvotes));

  console.log('Creating listing details')
  const exDescription = 'Lorem ipsum dolor sit amet, consectetur adipiscing \
    elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\
    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut \
    aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in \
    voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint\
    occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit\
    anim id est laborum.';  
  const exName = 'Listing Name';
  const exTagNames = ['Fundraiser', 'Event'];
  sectionListing.appendChild(createListingDetails(exDescription, exName, 
      exTagNames));

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
 * @param tagNames an array of Strings or tag names 
 * @return a div with the description, name, and tags of a listing
 */
function createListingDetails(description, name, tagNames) {
  const divListingDetails = createDivElement('', 'listing-info-container', '');

  console.log('Creating listing heading');
  divListingDetails.appendChild(createListingHeading(name, tagNames));

  divListingDetails.appendChild(
    createPElement(description, '', ''));

  return divListingDetails;
}

/**
 * Create an element with listing heading
 *
 * @param name the name of this listing
 * @param tagNames an array of Strings or tag names
 * @return a div with the name and tags of a listing.
 */
function createListingHeading(name, tagNames) {
  const divListingHeading = createDivElement('', 'listing-heading-container', 
      '');
  
  divListingHeading.appendChild(
    createHElement(name, 2, 'listing-preview-name', ''));

  console.log('Creating listing tags');
  divListingHeading.appendChild(createListingTags(tagNames));

  return divListingHeading;
}

/**
 * Create an element with listing tags
 *
 * @param tagNames an array of Strings or tag names
 * @return a div with the tags of a listing
 */
function createListingTags(tagNames) {
  const divListingTags = createDivElement('', 'listing-tags-container', '');

  for (let i = 0; i < tagNames.length; i ++) {
    divListingTags.appendChild(
      createSpanElement(tagNames[i], 'listing-tag', ''));  
  }

  return divListingTags;
}
