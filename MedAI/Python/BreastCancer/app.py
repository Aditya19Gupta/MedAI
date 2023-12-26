from fastapi import FastAPI, Request, Form
from fastapi.responses import HTMLResponse
import pickle
import numpy as np
import pandas as pd
import warnings
from sklearn.exceptions import InconsistentVersionWarning

warnings.filterwarnings("ignore", category=InconsistentVersionWarning)

app = FastAPI()
model = pickle.load(open('model.pkl', 'rb'))



@app.post("/predict")
def predict(request: Request,
            clump_thickness: int = Form(...), 
            uniform_cell_size: int = Form(...),
            uniform_cell_shape: int = Form(...),
            marginal_adhesion: int = Form(...),
            single_epithelial_size: int = Form(...),
            bare_nuclei : int = Form(...),
            bland_chromatin : int = Form(...),
            normal_nucleoli: int = Form(...),
            mitoses : int = Form(...),
            
            ):
    input_data = {
        'clump_thickness': clump_thickness,
        'uniform_cell_size': uniform_cell_size,
        'uniform_cell_shape': uniform_cell_shape, 
        'marginal_adhesion': marginal_adhesion,
        'single_epithelial_size': single_epithelial_size, 
        'bare_nuclei': bare_nuclei,
        'bland_chromatin' : bland_chromatin,  
        ' normal_nucleoli': normal_nucleoli, 
        'mitoses':mitoses
    }

    df = pd.DataFrame([input_data])

    prediction = model.predict(df)
    if prediction[0] == 4:
        res_val = "Breast cancer Detected.."
    else:
        res_val = "No Breast cancer Detected.."

    return res_val
