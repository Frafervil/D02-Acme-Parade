function validatePhone(message, countryCode) {
	var pattern = /^\+[1-9]\d{0,2}\([1-9]\d{0,2}\)\d{4,}|\+[1-9]\d{0,2} \([1-9]\d{0,2}\) \d{4,}|\+[1-9]\d{0,2}\d{4,}|\+[1-9]\d{0,2} \d{4,}$/;
	if (document.getElementById("phone").value != "") {
		if (document.getElementById("phone").value.match(pattern)) {
			document.getElementById("form").submit();

		} else {
			if (confirm(message)) {
				// document.getElementById("phone").value = countryCode + " " + document.getElementById("phone").value;
				document.getElementById("form").submit();

			} else {
				return false;
			}
		}
	} else {
		document.getElementById("phone").removeAttribute('value');
		document.getElementById("form").submit();
	}
};