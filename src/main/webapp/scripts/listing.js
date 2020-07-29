import { 
  createDivElement, 
  createSectionElement
} from './htmlElement.js';

import createListingPreview from './listing-preview.js'

import createListingDetailedView from './listing-detailed.js'

/**
 * Creates a div with a user's listings.
 *
 * @param listingsId the id of this element
 * @param listingsClass the class of this element
 * @param numListings the number of listings to display (CHANGE TO ARRAY)
 * @return a div with all of a user's listings.
 */
export default function createListings(listingsClass, listingsId, numListings) {
  const divListings = createDivElement('', listingsClass, listingsId);
  for (let i = 0; i < numListings; i ++) {
    const id = listingsId + (i + 1);
    divListings.appendChild(createListing(id));
  }

  return divListings;
}

function createListing(cardElementId) {
  const sectionListing = createSectionElement('', '');

  const cardElementDisplay = "flex";
  const divCardContainerElement = createDivElement('', 'card-container modal',
      cardElementId);

  console.log("Creating listing preview");
  sectionListing.appendChild(createListingPreview(cardElementDisplay, 
      cardElementId));
    
  sectionListing.appendChild(divCardContainerElement);
    
  console.log("Creating listing");
  createListingDetailedView(divCardContainerElement, cardElementDisplay, 
        cardElementId);  

  return sectionListing;
}