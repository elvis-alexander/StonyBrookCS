from django.http import JsonResponse


def ok_response():
    return JsonResponse({'status': 'OK'})


def err_response():
    return JsonResponse({'status': 'ERROR'})
