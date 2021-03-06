import {
  createDivElement,
  createPElement,
  createSpanElement
} from './htmlElement.js';

import {
  displayErrorMessage,
  isErrorMessage,
  isSuccessMessage,
  keyboardAccessible,
  toggleElementDisplay
} from './miscellaneous.js';

/**
 * Creates a delete button and confirm deletion modal. 
 * When the delete button is clicked, it opens up a confirm deletion modal.
 *
 * @param deleteButtonClass the class to give to the delete button.
 * @param deleteOnSuccessFunc the function to call when the deletion is
 *     successful.
 * @param name the name of the object that will be deleted.
 * @param query a String that represents a query.
 * @return A span element that represents a delete element.
 */
function createDeleteElements(deleteButtonClass, deleteOnSuccessFunc, name,
    query) {
  const deleteContainerDiv = createDivElement('', '', '');

  const deleteAlertDiv = createDeleteModal(deleteOnSuccessFunc, name,
      query);
  deleteContainerDiv.appendChild(createDeleteButton(deleteAlertDiv,
      deleteButtonClass));

  deleteContainerDiv.appendChild(deleteAlertDiv);

  return deleteContainerDiv;
}

/**
 * Creates a delete button that when clicked, will create a confirm deletion 
 *     modal.
 *
 * @param deleteAlertDiv the confirm deletion modal to toggle.
 * @param deleteButtonClass the class to give to the delete button.
 * @return an element that represents a delete button.
 */
function createDeleteButton(deleteAlertDiv, deleteButtonClass) {
  const deleteSpan = createSpanElement('delete', 'delete-icon material-icons ' +
      deleteButtonClass, '');

  const toggleDeleteModalFunc = (event) => { 
    // Create pop up asking if user is sure they want to delete the object.
    toggleElementDisplay('flex', deleteAlertDiv);
    event.stopPropagation();
  };

  keyboardAccessible(deleteSpan, toggleDeleteModalFunc);

  return deleteSpan;
}

/**
 * Creates an alert or a confirm deletion modal to confirm that the user wants 
 *     to delete the object.
 *
 * @param deleteOnSuccessFunc the function to call when the deletion is
 *     successful.
 * @param name the name of the entity to be deleted.
 * @param query a String that represents a query.
 * @return an element that represents a deletion modal.
 */
function createDeleteModal(deleteOnSuccessFunc, name, query) {
  const deleteModalDiv = createDivElement('', 'delete-modal modal', '');

  // When an element on this modal (including the modal) is clicked on, it will
  //     stop further propgation.
  const stopPropagationFunc = (event) => {
    event.stopPropagation();
  }
  keyboardAccessible(deleteModalDiv, stopPropagationFunc);

  // Creates the container element for the modal elements.
  const deleteContainerDiv = createDivElement('', 'delete-container', '');
  deleteModalDiv.appendChild(deleteContainerDiv);

  // Creates the error icon
  deleteContainerDiv.appendChild(
    createSpanElement('error_outline', 'material-icons modal-delete-icon', ''));

  deleteContainerDiv.appendChild(createDeleteModalMessage(name));

  deleteContainerDiv.appendChild(
      createDeleteModalButtons(deleteModalDiv, deleteOnSuccessFunc, query));

  return deleteModalDiv;
}

/**
 * Creates a delete warning message.
 * Highlights the name of the object to be deleted.
 *
 * @name the name of the object to delete.
 * @return an element that represents a warning message.
 */
function createDeleteModalMessage(name) {
  const message = createPElement('Are you sure you want to delete ', '',
      '');
  message.appendChild(
    createSpanElement(name, 'delete-name', ''));

  return message;
}

/**
 * Creates the delete and cancel buttons on the modal.
 *
 * @param deleteModalDiv toggles the display of the modal if the button is 
 *     clicked.
 * @param deleteOnSuccessFunc the function to call when the deletion is
 *     successful.
 * @param query a String that represents a query.
 * @return an elements that contains the delete and cancel buttons.
 */
function createDeleteModalButtons(deleteModalDiv, deleteOnSuccessFunc, query) {
  const buttonContainerDiv = createDivElement('', 'delete-button-container', '');

  buttonContainerDiv.appendChild(
      createDeleteModalDeleteButton(deleteOnSuccessFunc, query));
  buttonContainerDiv.appendChild(createDeleteModalCancelButton(deleteModalDiv));

  return buttonContainerDiv;
}

/**
 * Creates a delete button for the delete modal that will delete an entity when 
 *     clicked.
 *
 * @param deleteOnSuccessFunc the function that will be called when the entity 
 *     is deleted.
 * @param query a String that represents a query.
 * @return an element that represetns a delete button.
 */
function createDeleteModalDeleteButton(deleteOnSuccessFunc, query) {
  const deleteButton = createDivElement('', 'delete-button delete-modal-button',
      '');
  deleteButton.innerText = 'Delete';

  const deleteEntityFunc = () => {
    // Delete the object.
    deleteEntity(deleteOnSuccessFunc, query);
  };

  keyboardAccessible(deleteButton, deleteEntityFunc);

  return deleteButton;
}

/**
 * Creates a cancel button for the delete modal.
 *
 * @param deleteModalDiv toggles the display of the modal if the button is 
 *     clicked.
 * @return an element that represents a cancel button.
 */
function createDeleteModalCancelButton(deleteModalDiv) {
  const cancelButton = createDivElement('', 'cancel-button delete-modal-button',
      '');
  cancelButton.innerText = 'Cancel';

  const toggleDeleteModalFunc = () => {
    // Create pop up asking if user is sure they want to delete the object.
    toggleElementDisplay('flex', deleteModalDiv);
  };

  keyboardAccessible(cancelButton, toggleDeleteModalFunc);

  return cancelButton;
}

/**
 * Deletes an object.
 * If the deletion was successful, call the onSuccessFunc.
 *
 * @param onSuccessFunc the function to call when the deletion is successful.
 * @param query a String that represents a query.
 */
function deleteEntity(onSuccessFunc, query) {
  fetch(query, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'}
    })
        .then(response => response.json())
        .then((message) => {
          if (isErrorMessage(message)) {
            displayErrorMessage(message);
          } else if (isSuccessMessage(message)) {
            onSuccessFunc();
          } else {
            displayErrorMessage('Unexpected return statement');
          }
        });
}

export { createDeleteElements };
