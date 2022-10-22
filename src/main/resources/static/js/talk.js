//script source
function submit(){
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:3000/posts");
    xhr.setRequestHeader("content-type", "application/json; charset=UTF-8")
    const data = {title:'JavaScript', author: 'Lee'};
    xhr.send(JSON.stringify(data));

    xhr.onload = () => {
        if(xhr.status === 201){
            const res = JSON.parse(xhr.response);
            console.log(res);
        }else{
            console.log(xhr.status, xhr.statusText);
        }
    }
    // let myform = document.getElementById('form_analysis');
    // formData = new FormData(myform);
    // fetch('http://localhost:3000/statistics',{
    //     method:'POST',
    //     body : myform
    // });
}