/**
 * Checks if a text field is filled correctly and updates the page accordingly.
 * If a field is invalid, it and its instructions will be highlighted and
 * submissions will be disabled. If a field is made valid the highlights will
 * be removed and there will be a check to enable submission buttons.
 *
 * @param fieldId the id of the field being modified
 * @param instructionsId the id of the instructions to be followed for a field
 */
export default function validateInput(fieldId, instructionsId) {
  let field = document.getElementById(fieldId);
  let instructions = document.getElementById(instructionsId);

  if (field.checkValidity()) {
    // If field is valid
    if (field.classList.contains('invalid-input')) {
      field.classList.toggle('invalid-input');
      instructions.classList.toggle('invalid-input-instructions');
    }
    checkFields();
  } else {
    // If field is invalid
    if (!field.classList.contains('invalid-input')) {
      field.classList.toggle('invalid-input');
      instructions.classList.toggle('invalid-input-instructions');
    }
    disableSubmissions();
  }
}

/**
 * Performs a check on all of the page's input fields to determine if
 * submissions should be enabled.
 */
function checkFields() {
  let fields = document.querySelectorAll('input, textarea');
  let submissionsAllowed = true;

  for (let i = 0; i < fields.length; i++) {
    if (fields[i].checkValidity() === false) {
      submissionsAllowed = false;
    }
  }

  if (submissionsAllowed) {
    enableSubmissions();
  }
}

/**
 * Disables submission buttons and makes an instruction message visible.
 */
function disableSubmissions() {
  let submissionButtons = document.getElementsByClassName('submission-buttons');
  for (let i = 0; i < submissionButtons.length; i++) {
    submissionButtons[i].setAttribute('disabled', '');
  }
  let stillNeededSpan = document.getElementById('still-needed');
  stillNeededSpan.style.visibility = "visible";
}

/**
 * Enables submission buttons and makes an instruction message hidden.
 */
function enableSubmissions() {
  let submissionButtons = document.getElementsByClassName('submission-buttons');
  for (let i = 0; i < submissionButtons.length; i++) {
    submissionButtons[i].removeAttribute('disabled');
  }
  let stillNeededSpan = document.getElementById('still-needed');
  stillNeededSpan.style.visibility = "hidden";
}
