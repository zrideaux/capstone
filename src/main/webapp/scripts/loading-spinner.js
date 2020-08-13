import { 
  createDivElement
} from './htmlElement.js';

/**
 *  Appends a loader to a container element.
 * 
 * @param containerElement The element to append this loader to.
 * @param idAttribute The name of the id of this loader element 
 */
export default function addLoadingSpinner(containerElement, idAttribute) {
  containerElement.appendChild(
    createDivElement('', 'loading-spinner', idAttribute));
    console.log("Added loader");
}