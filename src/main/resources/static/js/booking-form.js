function loadLecturers() {
    var deptId = document.getElementById('departmentId').value;
    var lecturerSelect = document.getElementById('lecturerId');
    lecturerSelect.innerHTML = '<option value="">-- Đang tải... --</option>';
    if (!deptId) { lecturerSelect.innerHTML = '<option value="">-- Chọn Giảng viên --</option>'; return; }

    fetch('/student/api/lecturers?departmentId=' + deptId)
        .then(r => r.json())
        .then(data => {
            lecturerSelect.innerHTML = '<option value="">-- Chọn Giảng viên --</option>';
            data.forEach(l => {
                var opt = document.createElement('option');
                opt.value = l.id;
                opt.textContent = l.fullName + ' (' + (l.academicRank || '') + ')';
                lecturerSelect.appendChild(opt);
            });
        });
}

// Set min date to today
document.addEventListener('DOMContentLoaded', function() {
    var today = new Date().toISOString().split('T')[0];
    document.getElementById('sessionDate').setAttribute('min', today);
});
