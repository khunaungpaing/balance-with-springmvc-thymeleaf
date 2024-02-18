document.addEventListener('DOMContentLoaded',()=>{
    const pageSizeChangeSelect = document.getElementById('pageSizeChangeSelect')
    if(pageSizeChangeSelect){
        pageSizeChangeSelect.addEventListener('change',() => {
            const formId = pageSizeChangeSelect.getAttribute('data-form-id')
            const size = pageSizeChangeSelect.value
            console.log(size)
            const form = document.getElementById(formId)
            const sizeInput = document.createElement('input')
            sizeInput.setAttribute('type','hidden')
            sizeInput.setAttribute('name','size')
            sizeInput.setAttribute('value',size)

            form.appendChild(sizeInput)
            form.submit()
        })
    }
})