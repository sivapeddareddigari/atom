<script>
customElements.define('show-hello', class extends HTMLElement {
  connectedCallback() {
    const shadow = this.attachShadow({mode: 'open'});
    shadow.innerHTML = `<input type=\"text\" name=\"name\" id=\"NameInputInShowRoot\" class=\"A1_tY has-custom-focus\" value=\"\" placeholder=\"${this.getAttribute('name')}\" maxlength=\"100\">      `;
  }
});
</script>

<show-hello name="John"></show-hello>