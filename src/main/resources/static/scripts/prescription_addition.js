let prescriptionIndex = 0;

function addPrescription() {
    const container = document.getElementById('prescriptions-container');

    const block = document.createElement('div');
    block.className = 'row mb-2 prescription-block';
    block.innerHTML = `
            <div class="col-md-3">
                <input class="form-control" name="prescriptions[${prescriptionIndex}].medicine" 
                placeholder="Medicine" required/>
            </div>
            <div class="col-md-2">
                <input class="form-control" name="prescriptions[${prescriptionIndex}].dosage" 
                placeholder="Dasage" required/>
            </div>
            <div class="col-md-2">
                <select class="form-select" name="prescriptions[${prescriptionIndex}].frequency" required>
                    <option value="" disabled selected th:placeholder="#{pl.edu.hospital.record.selectFreq}">Frequency</option>
                    <option value="DAILY">Daily</option>
                    <option value="WEEKLY">Weekly</option>
                    <option value="MONTHLY">Monthly</option>
                    <option value="NEEDED">When needed</option>
                </select>
            </div>
            <div class="col-md-2">
                <input class="form-control" type="date" placeholder="start date"
                 name="prescriptions[${prescriptionIndex}].startDate" required/>
            </div>
            <div class="col-md-2">
                <input class="form-control" type="date" placeholder="end date" name="prescriptions[${prescriptionIndex}].endDate"/>
            </div>
            <div class="col-md-1">
                <button type="button" class="btn btn-sm btn-danger" onclick="removePrescription(this)">×</button>
            </div>
        `;
    container.appendChild(block);
    prescriptionIndex++;
}

function removePrescription(button) {
    button.closest('.prescription-block').remove();
}
