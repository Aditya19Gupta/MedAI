from fastapi import FastAPI, Request, Form
from fastapi.responses import HTMLResponse
import pickle
import numpy as np
import pandas as pd
import warnings
from sklearn.exceptions import InconsistentVersionWarning

warnings.filterwarnings("ignore", category=InconsistentVersionWarning)

app = FastAPI()
model = pickle.load(open('Diabetesmodel.pkl', 'rb'))


@app.post("/predict")
def predict(request: Request,
            Glucose: int = Form(...), 
            Insulin: int = Form(...),
            BMI: int = Form(...),
            Age: int = Form(...),
            
            ):
    input_data = {
        'Glucose': Glucose,
        'Insulin':Insulin,
        'BMI': BMI, 
        'Age': Age,
    }

    df = pd.DataFrame([input_data])

    prediction = model.predict(df)
    if prediction[0] == 1:
        res_val = "High Chances of Diabetes"
    else:
        res_val = "No Chances of Diabetes"

    return  res_val


