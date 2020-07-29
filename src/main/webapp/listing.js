import { 
  createDivElement, 
  createSectionElement
} from './htmlElement.js';

import createListingPreview from './listing-preview.js'

import {
  createListingDetailedView, 
} from './listing-detailed.js'

export default function createListing(cardElementId) {
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