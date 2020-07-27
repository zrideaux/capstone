import { 
  createDivElement, 
  createHElement, 
  createImgElement, 
  createPElement,
  createSectionElement,
  createSpanElement
  } from './htmlElement.js';

// miscellaneous functions

/** 
 * Toggles the display of an element. 
 * 
 * @param id used to get an element with this id
 */
function toggleDisplay(display, id) {
  let element = document.getElementById(id);
  const elementStyle = getComputedStyle(element, null).display;
  if (elementStyle === display) {
    console.log('Element with id ' + id + ' is now hidden.')
    console.log('Display: ' + elementStyle + ' is now ' + display);
    element.style.display = 'none';
  } else {
    console.log('Element with id ' + id + ' is now visible.')
    console.log('Display: ' + elementStyle + ' is now ' + display);
    element.style.display = display;
  }  
}

/**
 * Create an element that shows a listing detailed view
 *
 * @return a div with all the preview information pertaining to a listing
 */
export default function createListingPreview(listingDisplay, listingId) {

  const sectionListing = createSectionElement('listing shadow-box', '');
  // aLeadToExpendedView.appendChild(sectionListing);

  console.log("Creating listing information");
  sectionListing.appendChild(createListingInformation());

  console.log('Creating listing information')
  sectionListing.appendChild(createListingDetails());

  sectionListing.addEventListener("click", function(){ toggleDisplay(
      listingDisplay, listingId) });

  return sectionListing;
}

/**
 * Create an element with listing information
 *
 * @return a div with the picture, name, category, reputation, listing details 
 *     (see below) and website of a listing.
 */
function createListingInformation() {
  const divListingInfo = createDivElement('', '', '');

  divListingInfo.appendChild(
    createImgElement('', 'Listing preview image', 'listing-image', ''));

  divListingInfo.appendChild(
    createSpanElement('Reputation: 205 Votes', 'listing-reputation', ''));

  return divListingInfo;
}

/**
 * Create an element with listing details
 *
 * @return a div with the picture, name, category, and reputation of a listing
 */
function createListingDetails() {
  const divListingDetails = createDivElement('', 'listing-info-container', '');

  console.log('Creating listing heading');
  divListingDetails.appendChild(createListingHeading());

  const pDescription = 'Lorem ipsum dolor sit amet, consectetur adipiscing \
    elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\
    Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut \
    aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in \
    voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint\
    occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit\
    anim id est laborum.'
  divListingDetails.appendChild(
    createPElement(pDescription, '', ''));

  return divListingDetails;
}

/**
 * Create an element with listing heading
 *
 * @return a div with the name and tags of a listing.
 */
function createListingHeading() {
  const divListingHeading = createDivElement('', 'listing-heading-container', 
      '');
  
  divListingHeading.appendChild(
    createHElement('Listing Name', 2, '', ''));

  console.log("Creating listing tags");
  divListingHeading.appendChild(createListingTags());

  return divListingHeading;
}

/**
 * Create an element with listing tags
 *
 * @return a div with the tags of a listing
 */
function createListingTags() {
  const divListingTags = createDivElement('', 'listing-tags-container', '');

  divListingTags.appendChild(
    createSpanElement('Fundraiser', 'listing-tag fundraiser-tag', ''));

  divListingTags.appendChild(
    createSpanElement('Event', 'listing-tag event-tag', ''));

  return divListingTags;
}
