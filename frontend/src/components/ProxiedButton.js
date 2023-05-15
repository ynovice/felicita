import Api from "../Api";

function ProxiedButton ({href, redirect, style="", children}) {

    async function clickHandler(e) {
        e.preventDefault();

        if(await Api.logout()) {
            window.location.href = redirect;
        } else {
            alert("Что-то пошло не так");
        }
    }

    return (
        <a className={"button " + style} href={href} onClick={clickHandler}>
            {children}
        </a>
    );
}

export default ProxiedButton;