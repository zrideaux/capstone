import { 
  createDivElement, 
  createSectionElement
} from './htmlElement.js';

import createListingPreview from './listing-preview.js'

import createListingDetailedView from './listing-detailed.js'

/**
 * Creates a div with a user's listings.
 *
 * @param listings an array of json that represents a listing
 * @param listingsId the id of this element
 * @param listingsClass the class of this element
 * @return a div with all of a user's listings.
 */
export default function createListings(listings, listingsClass, listingsId) {
  const divListings = createDivElement('', listingsClass, listingsId);

  for (let i = 0; i < listings.length; i ++) {
    const listing = listings[i];
    const id = listingsId + (i + 1);
    divListings.appendChild(createListing(id, listing));
  }

  return divListings;
}

/**
 * Creates a section with a preview and a detailed listing
 * @param cardElementId the id of the section element containing the prewiew 
 *     and detailed listing
 * @param listing the json with listing information
 * @return a section that represents a listing
 */
function createListing(cardElementId, listing) {
  const sectionListing = createSectionElement('', '');

  const cardElementDisplay = "flex";
  // const divCardContainerElement = createDivElement('', 'card-container modal',
  //     cardElementId);

  console.log("Creating listing preview");
  sectionListing.appendChild(createListingPreview(listing, cardElementDisplay, 
      cardElementId));
    
  console.log("Creating listing view");
  sectionListing.appendChild(createListingDetailedView(listing, 
      cardElementDisplay, cardElementId));
    
  // sectionListing.appendChild(divCardContainerElement);
    
  // console.log("Creating listing");
  // createListingDetailedView(divCardContainerElement, cardElementDisplay, 
  //       cardElementId);  

  return sectionListing;
}